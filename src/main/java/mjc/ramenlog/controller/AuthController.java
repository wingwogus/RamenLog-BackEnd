package mjc.ramenlog.controller;

import jakarta.validation.Valid;
import mjc.ramenlog.dto.*;
import mjc.ramenlog.dto.jwt.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.service.inf.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtToken>> login(@RequestBody LoginRequestDto loginRequestDto) {
        JwtToken response = memberService.login(loginRequestDto);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<JwtToken>> reissue(@RequestBody ReissueRequestDto request) {
        JwtToken newToken = memberService.reissue(request);
        return ResponseEntity.ok(ApiResponse.success("RefreshToken 재발급 성공", newToken));
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<ApiResponse<Void>> sendMessage(@RequestBody EmailRequestDto emailRequestDto) {
        memberService.sendCodeToEmail(emailRequestDto.getEmail());

        return ResponseEntity.ok(ApiResponse.success("이메일 전송에 성공하였습니다", null));
    }

    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<Void>> verification(@RequestBody VerifiedRequestDto verifiedRequestDto) {

        memberService.verifiedCode(verifiedRequestDto);

        return ResponseEntity.ok(ApiResponse.success("코드 인증에 성공하였습니다.", null));
    }

    @PostMapping("/verification-nickname")
    public ResponseEntity<ApiResponse<Void>> verificationNickname(@RequestBody VerifiedNicknameRequest verifiedRequestDto) {
        memberService.checkDuplicatedNickname(verifiedRequestDto);

        return ResponseEntity.ok(ApiResponse.success("사용 가능한 닉네임입니다!", null));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> verificationEmail(@RequestBody SignUpRequestDto signUpRequest) {
        memberService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입에 성공하였습니다", null));
    }

    @PostMapping("/")
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsername());
    }
}