package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mjc.ramenlog.domain.Review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewResponseDto {
    @Schema(description = "리뷰 작성할 식당의 이름", example = "1")
    private String restaurantName;

    @Schema(description = "별점 (0.5 단위)", example = "4.5")
    private Double rating;

    @Schema(description = "리뷰 내용", example = "면이 쫄깃하고 국물이 오동통통!")
    private String content;

    private String nickname;

    private LocalDateTime createdAt;

    private List<String> images = new ArrayList<>();

    public ReviewResponseDto(Review review) {
        restaurantName = review.getRestaurant().getName();
        nickname = review.getMember().getNickname();
        rating = review.getRating();
        content = review.getContent();
        createdAt = review.getCreatedAt();
    }
}
