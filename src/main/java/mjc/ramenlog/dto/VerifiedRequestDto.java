package mjc.ramenlog.dto;

import lombok.Data;

@Data
public class VerifiedRequestDto {
    private String email;
    private String code;
}
