package mjc.ramenlog.controller;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.RestaurantResponseDto;
import mjc.ramenlog.jwt.CustomUserDetails;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.service.inf.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> SearchRestaurant(@RequestParam String keyword){
        List<Restaurant> result = restaurantRepository.findByNameContainingIgnoreCase(keyword);
        List<RestaurantResponseDto> dtoList = result.stream()
                .map(RestaurantResponseDto::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @GetMapping("/random")
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> getRandomRestaurant() {
        Restaurant random = restaurantService.getRandomRestaurant();
        RestaurantResponseDto dto = RestaurantResponseDto.from(random);
        return ResponseEntity.ok(ApiResponse.success("랜덤 추천 성공", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> getAllRestaurants() {
        List<Restaurant> all = restaurantRepository.findAll();
        List<RestaurantResponseDto> dtoList = all.stream()
                .map(RestaurantResponseDto::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("모든 목록 조회 성공", dtoList));
    }

    @GetMapping("/rank")
    public ResponseEntity<ApiResponse<List<RestaurantResponseDto>>> getRestaurantRanking() {
        List<RestaurantResponseDto> restaurantSortScore = restaurantService.getRestaurantSortScore();
        return ResponseEntity.ok(ApiResponse.success("랭킹 조회 성공", restaurantSortScore));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> getRestaurantById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        RestaurantResponseDto restaurant = restaurantService.getRestaurant(id, customUserDetails.getMember().getId());

        return ResponseEntity.ok(ApiResponse.success("레스토랑 조회 완료", restaurant));
    }
}
