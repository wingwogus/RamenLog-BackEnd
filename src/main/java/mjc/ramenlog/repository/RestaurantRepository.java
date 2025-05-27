package mjc.ramenlog.repository;

import mjc.ramenlog.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);

    boolean existsByNameAndAddressFullAddress(String name, String fullAddress);

    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
