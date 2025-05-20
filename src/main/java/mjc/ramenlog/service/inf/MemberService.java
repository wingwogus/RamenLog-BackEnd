package mjc.ramenlog.service;

import mjc.ramenlog.dto.LoginRequestDto;
import mjc.ramenlog.dto.ReissueRequestDto;
import mjc.ramenlog.dto.SignUpRequestDto;
import mjc.ramenlog.dto.jwt.JwtToken;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    JwtToken login(LoginRequestDto request);
    JwtToken reissue(ReissueRequestDto request);
    void signUp(SignUpRequestDto signUpRequestDto);
    void sendCodeToEmail(String toEmail);
    boolean verifiedCode(String email, String authCode);
}
