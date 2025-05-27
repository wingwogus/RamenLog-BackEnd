package mjc.ramenlog.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String fullAddress; // 네이버 api에서가져오는 주소는 하나의 도로명 전체 문자열.
                                // 만약 city, town street으로 나누려면 주소 파싱 로직 필요.
}

// 주소 파싱 로직
//public Address parseAddress(String roadAddress) {
//    String[] parts = roadAddress.split(" ", 3);
//    String city = parts.length > 0 ? parts[0] : "";
//    String town = parts.length > 1 ? parts[1] : "";
//    String street = parts.length > 2 ? parts[2] : "";
//    return new Address(city, town, street);
//}
