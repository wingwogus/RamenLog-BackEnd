package mjc.ramenlog.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.domain.Role;
import mjc.ramenlog.repository.MemberRepository;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;

    @PostConstruct
    public void init() {
        Member member1 = Member.builder()
                .email("user1@naver.com")
                .nickname("user1")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .build();

        Member member2 = Member.builder()
                .email("user2@naver.com")
                .nickname("user2")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
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
    }

}

