package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Role;
import mjc.ramenlog.dto.*;
import mjc.ramenlog.jwt.JwtToken;
import mjc.ramenlog.exception.*;
import mjc.ramenlog.jwt.JwtTokenProvider;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.SpotLikeRepository;
import mjc.ramenlog.service.inf.MemberService;
import mjc.ramenlog.service.inf.SpotLikeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    private final SpotLikeRepository spotLikeRepository;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private static final String VERIFIED_EMAIL_PREFIX = "VerifiedEmail ";

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
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public JwtToken reissue(ReissueRequestDto request) {
        jwtTokenProvider.validateToken(request.getRefreshToken());

        // 4. 새 토큰 생성
        return jwtTokenProvider.reissueToken(request.getAccessToken(), request.getRefreshToken());
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        String isSuccess = redisService.getValues(VERIFIED_EMAIL_PREFIX + signUpRequestDto.getEmail())
                .orElseThrow(() -> new NotVerifiedEmailException("인증되지 않은 이메일입니다."));

        if (!isSuccess.equals("true")) {
            throw new NotVerifiedEmailException("인증되지 않은 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .nickname(signUpRequestDto.getNickname())
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

    @Override
    public void logout(String email) {
        Optional<String> refreshToken = redisService.getValues("RT:" + email);
        if (refreshToken.isEmpty()) {
            throw new InvalidTokenException("로그인되어 있지 않은 상태입니다");
        }

        redisService.deleteValues("RT:" + email);
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new DuplicateEmailException(email);
        }
    }

    public void checkDuplicatedNickname(VerifiedNicknameRequest verifiedRequestDto) {
        Optional<Member> member = memberRepository.findByNickname(verifiedRequestDto.getNickname());
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedNickname exception occur nickname: {}", verifiedRequestDto.getNickname());
            throw new DuplicateNicknameException(verifiedRequestDto.getNickname());
        }
    }

    @Override
    public void verifiedCode(VerifiedRequestDto verifiedRequestDto) {
        String email = verifiedRequestDto.getEmail();
        String authCode = verifiedRequestDto.getCode();

        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email)
                .orElseThrow(() -> new VerificationFailedException("인증코드가 존재하지 않습니다. 다시 요청해주세요."));

        if (!redisAuthCode.equals(authCode)) {
            throw new VerificationFailedException("인증코드가 일치하지 않습니다.");
        }

        redisService.setValues(VERIFIED_EMAIL_PREFIX + email, "true");
    }

    @Override
    public MemberResponseDto getInformation(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<RestaurantResponseDto> spotLikes = spotLikeRepository
                .findByMember(member).stream()
                .map(spotLike -> RestaurantResponseDto.from(spotLike.getRestaurant()))
                .toList();

        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .grade(member.getGrade().getName())
                .profileImageUrl(member.getProfileImageUrl())
                .spotLike(spotLikes)
                .build();
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
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "코드 생성 오류 발생");
        }
    }
}
