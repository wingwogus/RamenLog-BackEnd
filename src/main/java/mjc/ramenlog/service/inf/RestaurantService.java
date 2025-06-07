package mjc.ramenlog.service.inf;

import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.RestaurantResponseDto;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDto getRestaurant(Long restaurantId, Long memberId);

    Restaurant getRandomRestaurant();

    List<RestaurantResponseDto> getRestaurantSortScore();
}
