package mjc.ramenlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Data;
import mjc.ramenlog.domain.Restaurant;

@Data
public class RestaurantDto {

    @Schema(description = "식당 id")
    private Long id;

    @Schema(description = "식당 이름")
    private String name;

    @Schema(description = "식당 주소")
    @Embedded
    private String address;

    @Schema(description = "식당 좌표 : 경도")
    private double longitude;

    @Schema(description = "식당 좌표 : 위도")
    private double latitude;

    @Schema(description = "식당 평균 평점")
    private double avgRating;

    @Schema(description = "이미지url")
    private String imageUrl;

    public static RestaurantDto from(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setAddress(r.getAddress().getFullAddress());
        dto.setLongitude(r.getLongitude());
        dto.setLatitude(r.getLatitude());
        dto.setAvgRating(r.getAvgRating());
        dto.setImageUrl(r.getImageUrl());
        return dto;
    }
}
