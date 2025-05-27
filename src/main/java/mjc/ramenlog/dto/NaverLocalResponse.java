package mjc.ramenlog.dto;

import lombok.Data;

import java.util.List;

@Data
public class NaverLocalResponse {
    private List<NaverLocalItem> items;
}

