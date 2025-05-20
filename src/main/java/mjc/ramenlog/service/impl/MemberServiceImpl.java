package mjc.ramenlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Role;
import mjc.ramenlog.dto.LoginRequestDto;
import mjc.ramenlog.dto.ReissueRequestDto;
import mjc.ramenlog.dto.SignUpRequestDto;
import mjc.ramenlog.dto.jwt.JwtToken;
import mjc.ramenlog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MailService mailService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Override
    public JwtToken login(LoginRequestDto request) {
        /// 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public JwtToken reissue(ReissueRequestDto request) {
        // 1. RefreshToken 유효성 검사
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // 2. AccessToken에서 사용자 정보 추출 (비록 만료되었어도 subject 추출 가능)
        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());

        // 3. 저장된 refreshToken 과 비교 (선택: Redis나 DB에 저장 시)
        // String stored = refreshTokenRepository.findByUsername(authentication.getName());
        // if (!stored.equals(request.getRefreshToken())) throw new ...

        // 4. 새 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        Member member = Member.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .nickname(signUpRequestDto.getNickname())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    @Override
    public void sendCodeToEmail(String toEmail) {
        checkDuplicatedEmail(toEmail);
        String title = "RamenLog 이메일 인증 번호";
        String authCode = createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new RuntimeException("이미 존재하는 이메일입니다!");
        }
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new RuntimeException();
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        return redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }
}
