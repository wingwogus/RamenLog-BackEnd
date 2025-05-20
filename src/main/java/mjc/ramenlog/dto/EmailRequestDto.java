package mjc.ramenlog.dto;

import jakarta.validation.Valid;
import lombok.Data;
import mjc.ramenlog.annotation.CustomEmail;

@Data
public class EmailRequestDto {

    @CustomEmail @Valid
    private String email;
}
