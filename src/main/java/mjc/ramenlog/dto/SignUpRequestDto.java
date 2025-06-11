package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @Schema(description = "이메일", example = "example@naver.com")
    private String email;

    @Schema(description = "비밀번호", example = "*12341234")
    private String password;

    @Schema(description = "닉네임", example = "거인이재현")
    private String nickname;
}
