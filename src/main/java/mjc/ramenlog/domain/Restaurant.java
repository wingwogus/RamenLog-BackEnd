package mjc.ramenlog.domain;

import jakarta.persistence.*;
import lombok.*;


import java.io.DataOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private Double score;

    private String description;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private boolean isOpen;

    private double latitude; //위도
    private double longitude; //경도

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantImage> image = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<SpotLike> spotLike = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Review> review = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RestaurantRamen> restaurantRamen = new ArrayList<>();

    public void setScore(double score) {
        this.score = score;
    }
}
