package mjc.ramenlog.domain;

import jakarta.persistence.*;

@Entity
public class RestaurantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String imageUrl;
}
