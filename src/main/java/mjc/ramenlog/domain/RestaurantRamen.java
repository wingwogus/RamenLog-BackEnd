package mjc.ramenlog.domain;

import jakarta.persistence.*;

@Entity
public class RestaurantRamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "ramen_id")
    private Ramen ramen;

    private String name;

    private String imageUrl;

    private String price;
}
