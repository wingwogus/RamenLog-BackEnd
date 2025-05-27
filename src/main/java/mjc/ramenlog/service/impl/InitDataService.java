package mjc.ramenlog.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.*;
import mjc.ramenlog.repository.GradeRepository;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.RestaurantRepository;
import mjc.ramenlog.repository.SpotLikeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final SpotLikeRepository spotLikeRepository;
    private final GradeRepository gradeRepository;

    @PostConstruct
    public void init() {

        Grade grade = Grade.builder()
                .name("라오타")
                .count(10)
                .build();

        gradeRepository.save(grade);


        Member member1 = Member.builder()
                .email("user1@naver.com")
                .nickname("user1")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .grade(grade)
                .build();

        Member member2 = Member.builder()
                .email("user2@naver.com")
                .nickname("user2")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .grade(grade)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Restaurant restaurant = Restaurant.builder()
                .name("맛스구")
                .address(Address.builder()
                        .city("서울특별시")
                        .town("은평구")
                        .street("응암동")
                        .build())
                .score(100)
                .build();

        restaurantRepository.save(restaurant);

        SpotLike spotLike = SpotLike.builder()
                .member(member1)
                .restaurant(restaurant)
                .build();

        spotLikeRepository.save(spotLike);
    }

}

