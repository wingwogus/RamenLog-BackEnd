package mjc.ramenlog.service.inf;

import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.RestaurantResponseDto;

import java.util.List;

public interface RestaurantService {
    Restaurant getRandomRestaurant();

    List<RestaurantResponseDto> getRestaurantSortScore();
}
