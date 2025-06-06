package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.service.inf.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ResturantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant getRandomRestaurant() {
        List<Restaurant> all = restaurantRepository.findAll();
        if (all.isEmpty()) {
            throw new NoSuchElementException("추천할 식당이 없습니다.");
        }

        int randomIndex = new Random().nextInt(all.size());
        return all.get(randomIndex);
    }
}
