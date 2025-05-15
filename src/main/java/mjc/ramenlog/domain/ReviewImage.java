package mjc.ramenlog.domain;

import jakarta.persistence.*;

@Entity
public class ReviewImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String imageUrl;
}
