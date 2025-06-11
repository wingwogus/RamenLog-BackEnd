package mjc.ramenlog.service.inf;

import mjc.ramenlog.dto.RestaurantResponseDto;

import java.util.List;

public interface SpotLikeService {

    List<RestaurantResponseDto> getLikedRestaurants(Long memberId);

    void addLike(Long spotId, Long memberId);

    void removeLike(Long spotId, Long memberId);

    boolean toggleLike(Long spotId, Long memberId);
}
