package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;

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

    @Schema(description = "식당 평점")
    private Double avgRating;

    @Schema(description = "식당 이미지")
    private String imageUrl;


    public static RestaurantResponseDto from(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .avgRating(restaurant.getAvgRating())
                .imageUrl(restaurant.getImageUrl())
                .build();
    }
}
