package mjc.ramenlog.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Member;
import mjc.ramenlog.domain.Role;
import mjc.ramenlog.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDataService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member member1 = Member.builder()
                .email("user1@naver.com")
                .nickname("user1")
                .phoneNumber("010-0000-0000")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .build();

        Member member2 = Member.builder()
                .email("user2@naver.com")
                .nickname("user2")
                .phoneNumber("010-0000-0000")
                .role(Role.USER)
                .profileImageUrl("https://ui-avatars.com/api/?name=Jae+Hyun&background=random")
                .password(passwordEncoder.encode("1234"))
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

}

