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
    }

}

