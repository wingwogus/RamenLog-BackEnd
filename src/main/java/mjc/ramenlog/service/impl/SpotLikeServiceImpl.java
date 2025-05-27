package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.domain.SpotLike;
import mjc.ramenlog.dto.RestaurantResponseDto;
import mjc.ramenlog.exception.AlreadySpotLikeException;
import mjc.ramenlog.exception.NotFoundMemberException;
import mjc.ramenlog.exception.NotFoundRestaurantException;
import mjc.ramenlog.exception.NotFoundSpotLikeException;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.repository.SpotLikeRepository;
import mjc.ramenlog.service.inf.SpotLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotLikeServiceImpl implements SpotLikeService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final SpotLikeRepository spotLikeRepository;

    @Override
    public List<RestaurantResponseDto> getLikedRestaurants(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        return spotLikeRepository.findByMember(member).stream()
                .map(spotLike -> RestaurantResponseDto.from(spotLike.getRestaurant()))
                .toList();
    }

    @Override
    public void addLike(Long spotId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Restaurant restaurant = restaurantRepository.findById(spotId)
                .orElseThrow(NotFoundRestaurantException::new);

        spotLikeRepository
                .findByRestaurantAndMember(restaurant, member)
                .ifPresent(like -> { throw new AlreadySpotLikeException();});

        spotLikeRepository.save(
                SpotLike.builder()
                .restaurant(restaurant)
                .member(member)
                .build());
    }

    @Override
    public void removeLike(Long spotId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Restaurant restaurant = restaurantRepository.findById(spotId)
                .orElseThrow(NotFoundRestaurantException::new);

        SpotLike like = spotLikeRepository
                .findByRestaurantAndMember(restaurant, member)
                .orElseThrow(NotFoundSpotLikeException::new);

        spotLikeRepository.delete(like);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long spotId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Restaurant restaurant = restaurantRepository.findById(spotId)
                .orElseThrow(NotFoundRestaurantException::new);

        return spotLikeRepository.findByRestaurantAndMember(restaurant, member)
                .map(like -> {
                    // 있으면 삭제, false 리턴
                    spotLikeRepository.delete(like);
                    return false;
                })
                .orElseGet(() -> {
                    // 없으면 저장, true 리턴
                    spotLikeRepository.save(
                            SpotLike.builder()
                                    .restaurant(restaurant)
                                    .member(member)
                                    .build());
                    return true;
                });
    }
}
