package mjc.ramenlog.dto;

import lombok.Data;
import mjc.ramenlog.domain.Restaurant;

@Data
public class RestaurantDto {
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private double avgRating;

    public static RestaurantDto from(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setName(r.getName());
        dto.setAddress(r.getAddress().getFullAddress());
        dto.setLongitude(r.getLongitude());
        dto.setLatitude(r.getLatitude());
        dto.setAvgRating(r.getAvgRating());
        return dto;
    }
}
