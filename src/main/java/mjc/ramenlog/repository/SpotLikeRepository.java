package mjc.ramenlog.repository;

import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.domain.SpotLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotLikeRepository extends JpaRepository<SpotLike, Long> {
    Optional<SpotLike> findByRestaurantAndMember(Restaurant restaurant, Member member);

    Optional<SpotLike> findByMember(Member member);
}
