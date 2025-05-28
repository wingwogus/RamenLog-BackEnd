package mjc.ramenlog.repository;

import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByMember(Member member);
}
