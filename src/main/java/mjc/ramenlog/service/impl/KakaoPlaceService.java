package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.KakaoApiResponse;
import mjc.ramenlog.dto.KakaoPlace;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@RequiredArgsConstructor
public class KakaoPlaceService {

    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String KAKAO_API_KEY = "1e79ef1db1ffb08edf2bda1e71a9f8a7";
    private final RestClient.Builder builder;

    public void searchAndSaveRamenShops(String keyword) {
        int page = 1;
        boolean isEnd = false;

        while (!isEnd && page <= 45) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            String url = KAKAO_API_URL + "?query=" + keyword + "&page=" + page + "&size=15";
            HttpEntity<?> entity = new HttpEntity<>(headers);


            ResponseEntity<KakaoApiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KakaoApiResponse.class
            );

            KakaoApiResponse kakaoResponse = response.getBody();
            if (kakaoResponse != null && kakaoResponse.getDocuments() != null) {
                for (KakaoPlace place : kakaoResponse.getDocuments()) {
                    Restaurant r = Restaurant.builder()
                            .name(place.getPlaceName())
                            .address(new Address(place.getAddressName()))
                            .latitude(Double.parseDouble(place.getY()))
                            .longitude(Double.parseDouble(place.getX()))
                            .score(6.0)
                            .avgRating(3.0)
                            .build();

                    System.out.println("저장 시도: " + r.getName() );
                    System.out.println("주소: " + r.getAddress().getFullAddress());
                    System.out.println("위도: " + r.getLatitude() + ", 경도: " + r.getLongitude());

                    restaurantRepository.save(r);

                }
                isEnd = kakaoResponse.getMeta().isEnd();
                page++;
            } else {
                isEnd = true;
            }
        }

    }
}
