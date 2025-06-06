package mjc.ramenlog.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.*;
import mjc.ramenlog.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final SpotLikeRepository spotLikeRepository;
    private final ReviewRepository reviewRepository;

    @PostConstruct
    public void init() {
        Member member1 = Member.builder()
                .email("user1@naver.com")
                .nickname("user1")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .grade(Grade.RAMEN_NOOB)
                .build();

        Member member2 = Member.builder()
                .email("user2@naver.com")
                .nickname("user2")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .grade(Grade.RAMEN_LEGEND)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Restaurant restaurant1 = Restaurant.builder()
                .name("맛스구")
                .address(Address.builder()
                        .fullAddress("서울시 은평구 응암동")
                        .build())
                .score(100.0)
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .name("옥토끼")
                .address(Address.builder()
                        .fullAddress("서울시 은평구 응암동")
                        .build())
                .score(45.0)
                .build();

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        SpotLike spotLike = new SpotLike(restaurant1, member1);
        SpotLike spotLike2 = new SpotLike(restaurant2, member1);

        spotLikeRepository.save(spotLike);
        spotLikeRepository.save(spotLike2);

        Review review1 = Review.builder()
                .content("앙 기모띄")
                .rating(3.5)
                .build();
        review1.setRestaurantAndMember(restaurant1,  member1);

        Review review2 = Review.builder()
                .content("앙 기모띄")
                .rating(3.5)
                .build();
        review2.setRestaurantAndMember(restaurant2,  member1);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
    }

}

