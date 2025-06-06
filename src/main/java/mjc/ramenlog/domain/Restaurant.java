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

    private Double score; // 랭킹 선정용 스코어
    private Double avgRating; // 평균 별점용 레이팅

    private double latitude; //위도
    private double longitude; //경도

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantImage> image = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private List<SpotLike> spotLike = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private List<Review> review = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantRamen> restaurantRamen = new ArrayList<>();

    public void setScore(double score) {
        this.score = score;
    }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
}
