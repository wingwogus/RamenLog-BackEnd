package mjc.ramenlog.service.inf;

import mjc.ramenlog.dto.*;
import mjc.ramenlog.dto.jwt.JwtToken;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    JwtToken login(LoginRequestDto request);
    JwtToken reissue(ReissueRequestDto request);
    void signUp(SignUpRequestDto signUpRequestDto);
    void sendCodeToEmail(String toEmail);
    void verifiedCode(VerifiedRequestDto verifiedRequestDto);
    void checkDuplicatedNickname(VerifiedNicknameRequest verifiedRequestDto);
}
