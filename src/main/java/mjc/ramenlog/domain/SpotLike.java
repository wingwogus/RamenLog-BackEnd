package mjc.ramenlog.domain;

import jakarta.persistence.*;

@Entity
public class SpotLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
