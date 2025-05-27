package mjc.ramenlog.dto;

import lombok.Data;

@Data
public class NaverLocalItem {
    private String title; //HTML 포함된 상호명
    private String link;
    private String description;
    private String category;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapX;
    private String mapY;
}
