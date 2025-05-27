package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.NaverLocalItem;
import mjc.ramenlog.dto.NaverLocalResponse;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;



@Service
@RequiredArgsConstructor
@Transactional
public class NaverPlaceService {

    private final RestaurantRepository restaurantRepository;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public void searchAndSaveRamenShops(String keyword) {
        try {

            String url = "https://openapi.naver.com/v1/search/local.json?query=라멘+" + keyword + "&display=10";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<NaverLocalResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, NaverLocalResponse.class);

            for (NaverLocalItem item : response.getBody().getItems()) {
                if (!isRamenCategory(item.getCategory())) continue;

                String fullAddress = item.getAddress();
                String name = item.getTitle().replaceAll("<[^>]*>", "");

                System.out.println("name = " + name);
                System.out.println("fullAddress = " + fullAddress);
                System.out.println("존재 여부 = " + restaurantRepository.existsByNameAndAddressFullAddress(name, fullAddress));

                if (restaurantRepository.existsByNameAndAddressFullAddress(name,fullAddress)) continue;

                System.out.println("저장 시도: " + name + ", " + fullAddress); // 여기에 추가

                Restaurant r = new Restaurant();
                r.setName(name);
                r.setDescription(item.getDescription());
                r.setAddress(new Address(item.getAddress()));
                r.setOpen(true);
                restaurantRepository.save(r);
            }
        }catch (Exception e) {
            System.err.println("Naver API 에러 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isRamenCategory(String category) {
        return category != null && category.contains("라멘");
    }
}
