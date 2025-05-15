package mjc.ramenlog.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Ramen {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ramen", fetch = FetchType.LAZY)
    private List<RestaurantRamen> restaurantRamen = new ArrayList<>();
}
