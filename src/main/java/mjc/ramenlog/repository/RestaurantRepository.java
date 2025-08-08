package mjc.ramenlog.repository;

import mjc.ramenlog.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);

    boolean existsByNameAndAddressFullAddress(String name, String fullAddress);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // score 기준 내림차순 정렬
    Optional<List<Restaurant>> findTop10ByOrderByScoreDesc();

    Page<Restaurant> findAllByAddressFullAddressContainingIgnoreCase(String keyword, Pageable pageable);

    boolean existsByPlaceId(String placeId);
}

