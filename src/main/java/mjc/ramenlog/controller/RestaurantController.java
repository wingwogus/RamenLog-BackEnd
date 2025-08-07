package mjc.ramenlog.controller;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.RestaurantResponseDto;
import mjc.ramenlog.jwt.CustomUserDetails;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.service.inf.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<Page<RestaurantResponseDto>>> getAllRestaurants(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (customUserDetails == null) {
            return ResponseEntity.ok(ApiResponse.success("모든 목록 조회 성공", restaurantService.getAllRestaurant(pageable)));
        }

        return ResponseEntity.ok(
                ApiResponse.success("모든 목록 조회 성공",
                        restaurantService.getAllRestaurant(customUserDetails.getMember().getId(), pageable)));
    }

    @GetMapping("/by-address")
    public ResponseEntity<ApiResponse<Page<RestaurantResponseDto>>> getAllRestaurantsByAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (customUserDetails == null) {
            return ResponseEntity.ok(ApiResponse.success(
                    address + " 목록 조회 성공",
                    restaurantService.getAllRestaurantByAddress(address, pageable)));
        }

        return ResponseEntity.ok(
                ApiResponse.success("모든 목록 조회 성공",
                        restaurantService.getAllRestaurantByAddress(customUserDetails.getMember().getId(), address, pageable)));
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

        if (customUserDetails == null) {
            return ResponseEntity.ok(ApiResponse.success("레스토랑 조회 완료", restaurantService.getRestaurant(id)));
        }

        return ResponseEntity.ok(ApiResponse.success("레스토랑 조회 완료",
                    restaurantService.getRestaurant(id, customUserDetails.getMember().getId())));
    }
}
