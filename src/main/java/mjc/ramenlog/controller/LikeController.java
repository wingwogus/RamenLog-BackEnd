package mjc.ramenlog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.RestaurantResponseDto;
import mjc.ramenlog.jwt.CustomUserDetails;
import mjc.ramenlog.service.inf.SpotLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Like", description = "라멘집 찜 API")
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final SpotLikeService spotLikeService;

    @GetMapping("/")
    @Operation(summary = "찜 목록", description = "사용자가 찜한 라멘집 목록을 불러옵니다.")
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> like(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RestaurantResponseDto> likedRestaurants =
                spotLikeService.getLikedRestaurants(userDetails.getMember().getId());

        return ResponseEntity.ok(ApiResponse.success("좋아요 조회 성공", likedRestaurants));
    }

    @PostMapping("/{restaurantId}/like")
    @Operation(summary = "찜하기", description = "라멘집 id를 사용해 찜 합니다.")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean liked =
                spotLikeService.toggleLike(restaurantId, userDetails.getMember().getId());

        return ResponseEntity.ok(ApiResponse.success(liked));
    }

}
