package mjc.ramenlog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<SpotLike> spotLike = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Review> review = new ArrayList<>();
}
