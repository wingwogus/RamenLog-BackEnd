package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.RestaurantResponseDto;
import mjc.ramenlog.exception.NotFoundMemberException;
import mjc.ramenlog.exception.NotFoundRestaurantException;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.repository.SpotLikeRepository;
import mjc.ramenlog.service.inf.RestaurantService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SpotLikeRepository spotLikeRepository;
    private final MemberRepository memberRepository;

    @Override
    public RestaurantResponseDto getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        return RestaurantResponseDto.from(restaurant);
    }

    @Override
    public RestaurantResponseDto getRestaurant(Long restaurantId, Long memberId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);


        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        RestaurantResponseDto dto = RestaurantResponseDto.from(restaurant);

        spotLikeRepository.findByRestaurantAndMember(restaurant, member)
                .ifPresent(spotLike -> dto.setLiked(true));

        return dto;
    }

    @Override
    public Page<RestaurantResponseDto> getAllRestaurant(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(RestaurantResponseDto::from);
    }

    @Override
    public Page<RestaurantResponseDto> getAllRestaurant(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        return restaurantRepository.findAll(pageable)
                .map(restaurant -> {
                    RestaurantResponseDto dto = RestaurantResponseDto.from(restaurant);
                    spotLikeRepository.findByRestaurantAndMember(restaurant, member)
                            .ifPresent(spotLike -> dto.setLiked(true));
                    return dto;
                });
    }

    @Override
    public Restaurant getRandomRestaurant() {
        List<Restaurant> all = restaurantRepository.findAll();
        if (all.isEmpty()) {
            throw new NoSuchElementException("추천할 식당이 없습니다.");
        }

        int randomIndex = new Random().nextInt(all.size());
        return all.get(randomIndex);
    }

    @Override
    public List<RestaurantResponseDto> getRestaurantSortScore() {
        List<Restaurant> restaurants = restaurantRepository.findTop10ByOrderByScoreDesc()
                .orElseThrow(NotFoundRestaurantException::new);

        return restaurants.stream()
                .map(RestaurantResponseDto::from)
                .toList();
    }

    @Override
    public Page<RestaurantResponseDto> getAllRestaurantByAddress(String keyword, Pageable pageable) {
        return restaurantRepository.findAllByAddressFullAddressContainingIgnoreCase(keyword, pageable)
                .map(RestaurantResponseDto::from);
    }

    @Override
    public Page<RestaurantResponseDto> getAllRestaurantByAddress(Long memberId, String keyword, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        return restaurantRepository.findAllByAddressFullAddressContainingIgnoreCase(keyword, pageable)
                .map(restaurant -> {
                    RestaurantResponseDto dto = RestaurantResponseDto.from(restaurant);
                    spotLikeRepository.findByRestaurantAndMember(restaurant, member)
                            .ifPresent(spotLike -> dto.setLiked(true));
                    return dto;
                });
    }
}
