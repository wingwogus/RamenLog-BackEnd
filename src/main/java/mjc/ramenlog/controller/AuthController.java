package mjc.ramenlog;

import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.LoginRequestDto;
import mjc.ramenlog.dto.LoginResponseDto;
import mjc.ramenlog.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = memberService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }
}