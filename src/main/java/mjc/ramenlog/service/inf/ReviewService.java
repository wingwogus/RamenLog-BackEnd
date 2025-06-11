package mjc.ramenlog.service.inf;

import mjc.ramenlog.dto.ReviewRequestDto;
import mjc.ramenlog.dto.ReviewResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void saveReview(Long memberId, ReviewRequestDto dto, List<MultipartFile> images);

    List<ReviewResponseDto> listReviewByMember(Long memberId);

    List<ReviewResponseDto> listReviewByRestaurant(Long restaurantId);
}
