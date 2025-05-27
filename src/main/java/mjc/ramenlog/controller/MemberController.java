package mjc.ramenlog.controller;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.dto.MemberResponseDto;
import mjc.ramenlog.jwt.CustomUserDetails;
import mjc.ramenlog.service.inf.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<MemberResponseDto>> findMemberInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberResponseDto information = memberService.getInformation(userDetails.getMember().getId());

        return ResponseEntity.ok(ApiResponse.success("정보 조회 성공", information));

    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> findOtherMemberInfo(@PathVariable Long memberId) {
        MemberResponseDto information = memberService.getInformation(memberId);

        return ResponseEntity.ok(ApiResponse.success("정보 조회 성공", information));
    }


}
