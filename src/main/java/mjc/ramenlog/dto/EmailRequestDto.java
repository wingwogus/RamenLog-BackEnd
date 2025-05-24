package mjc.ramenlog.dto;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mjc.ramenlog.annotation.CustomEmail;

@Data
public class EmailRequestDto {

    @CustomEmail
    @Valid
    @Schema(description = "사용자 이메일 주소", example = "user@example.com", required = true)
    private String email;
}
