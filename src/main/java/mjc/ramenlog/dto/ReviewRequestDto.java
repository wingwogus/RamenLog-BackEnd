package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewRequestDto {
    @Schema(description = "리뷰 작성할 식당의 ID", example = "1")
    private Long restaurantId;

    @Schema(description = "별점 (0.5 단위)", example = "4.5")
    private Double rating;

    @Schema(description = "리뷰 내용", example = "면이 쫄깃하고 국물이 오동통통!")
    private String content;
}
