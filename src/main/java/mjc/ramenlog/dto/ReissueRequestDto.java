package mjc.ramenlog.dto;

import lombok.Data;

@Data
public class ReissueRequestDto {
    private String accessToken;
    private String refreshToken;
}
