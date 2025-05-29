package mjc.ramenlog.dto;

import lombok.Data;
import mjc.ramenlog.domain.Restaurant;

@Data
public class RestaurantDto {
    private String name;
    private String address;

    public static RestaurantDto from(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setName(r.getName());
        dto.setAddress(r.getAddress().getFullAddress());
        return dto;
    }
}
