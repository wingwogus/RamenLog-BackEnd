package mjc.ramenlog.controller;

import com.fasterxml.classmate.members.ResolvedField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.ReviewRequestDto;
import mjc.ramenlog.dto.ReviewResponseDto;
import mjc.ramenlog.jwt.CustomUserDetails;
import mjc.ramenlog.service.inf.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    @Operation(summary = "리뷰 작성", description = "리뷰 텍스트와 최대 3장의 이미지를 업로드합니다.")
    public ResponseEntity<ApiResponse<String>> createReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewRequestDto dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Long memberId = userDetails.getMember().getId();

        reviewService.saveReview(memberId, dto, images);

        return ResponseEntity.ok(ApiResponse.success("리뷰 등록 성공"));
    }

    @GetMapping("/")
    public  ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviews(
            @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();

        List<ReviewResponseDto> data = reviewService.listReviewByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 조회 성공", data));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviewsByRestaurant(@PathVariable Long restaurantId){
        List<ReviewResponseDto> data = reviewService.listReviewByRestaurant(restaurantId);

        return ResponseEntity.ok(ApiResponse.success("리뷰 조회 성공", data));
    }
}
