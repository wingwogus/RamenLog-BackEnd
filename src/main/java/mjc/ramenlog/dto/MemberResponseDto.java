package mjc.ramenlog.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import mjc.ramenlog.domain.Grade;
import mjc.ramenlog.domain.Review;
import mjc.ramenlog.domain.Role;
import mjc.ramenlog.domain.SpotLike;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MemberResponseDto {

    private Long id;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private String grade;

    private List<RestaurantResponseDto> spotLike = new ArrayList<>();

//    private List<Review> review = new ArrayList<>();
}
