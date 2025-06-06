package mjc.ramenlog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoResponseDto {

    private Long id;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private String grade;

    private String nextGrade;

    private int startReviewCount;

    private int endReviewCount;

    private int remainingReviewCount;

    private int reviewCount;

    private int likeCount;
}
