package mjc.ramenlog.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import mjc.ramenlog.annotation.CustomEmail;

@Data
public class LoginRequestDto {

    @CustomEmail
    @Email
    @Schema(description = "사용자 이메일 주소", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "사용자 비밀번호", example = "password123", required = true)
    private String password;
}
