package mjc.ramenlog.service.inf;

import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDto getRestaurant(Long restaurantId);

    RestaurantResponseDto getRestaurant(Long restaurantId, Long memberId);

    Page<RestaurantResponseDto> getAllRestaurant(Pageable pageable);

    Page<RestaurantResponseDto> getAllRestaurant(Long memberId, Pageable pageable);

    Restaurant getRandomRestaurant();

    List<RestaurantResponseDto> getRestaurantSortScore();

    Page<RestaurantResponseDto> getAllRestaurantByAddress(String keyword, Pageable pageable);

    Page<RestaurantResponseDto> getAllRestaurantByAddress(Long memberId, String keyword, Pageable pageable);
}
