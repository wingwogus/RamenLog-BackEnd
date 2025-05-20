package mjc.ramenlog.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
}
