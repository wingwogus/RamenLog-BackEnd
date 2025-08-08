package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;

import java.util.List;

@Data
@Builder
public class RestaurantResponseDto {
    @Schema(description = "식당 ID")
    private Long id;

    @Schema(description = "식당 이름")
    private String name;

    @Schema(description = "식당 주소")
    @Embedded
    private Address address;

    @Schema(description = "식당 점수")
    private Double score;

    @Schema(description = "식당 평점")
    private Double avgRating;

    @Schema(description = "식당 이미지")
    private String imageUrl;

    @Schema(description = "식당 좌표 : 경도")
    private double longitude;

    @Schema(description = "식당 좌표 : 위도")
    private double latitude;

    @Setter
    @Schema(description = "식당 찜 여부")
    private boolean isLiked;

    private String phoneNumber;

    private Boolean openNow;

    private List<String> weekdayText;


    public static RestaurantResponseDto from(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .score(restaurant.getScore())
                .avgRating(restaurant.getAvgRating())
                .imageUrl(restaurant.getImageUrl())
                .phoneNumber(restaurant.getPhoneNumber())
                .openNow(restaurant.getOpenNow())
                .weekdayText(restaurant.getWeekdayText())
                .build();
    }
}
