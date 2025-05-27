package mjc.ramenlog.controller;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.RestaurantDto;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@CrossOrigin
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RestaurantDto>>> SearchRestaurant(@RequestParam String keyword){
        List<Restaurant> result = restaurantRepository.findByNameContainingIgnoreCase(keyword);
        List<RestaurantDto> dtoList = result.stream()
                .map(RestaurantDto::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }
}
