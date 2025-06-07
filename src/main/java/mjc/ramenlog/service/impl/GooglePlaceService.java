package mjc.ramenlog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.GoogleApiResponse;
import mjc.ramenlog.dto.KakaoApiResponse;
import mjc.ramenlog.dto.KakaoPlace;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class GooglePlaceService {
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;

    public void findRestaurantAndSave() {
        List<String> regions = List.of(
                // 서울 주요 구
                "서울 강남구", "서울 마포구", "서울 종로구", "서울 용산구", "서울 서대문구", "서울 중구", "서울 송파구",
                "서울 동작구", "서울 관악구", "서울 은평구", "서울 노원구", "서울 성동구", "서울 강서구", "서울 구로구"
                // 전국 시도
//                "부산", "대구", "인천", "광주", "대전", "울산", "세종",
//                "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"
        );

        Set<String> savedNames = new HashSet<>();

        for (String region : regions) {
            String nextToken = null;
            boolean isFirstRequest = true;

            while (isFirstRequest || nextToken != null) {
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                        + region + " 라멘"
                        + "&key=AIzaSyA1XCQwvYTPxSnM-PkSzSePWij3BwxZh7Q"
                        + "&language=ko";

                if (!isFirstRequest) {
                    url += "&pagetoken=" + nextToken;
                }

                ResponseEntity<GoogleApiResponse> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        GoogleApiResponse.class
                );

                GoogleApiResponse googleApiResponse = response.getBody();
                if (googleApiResponse != null && googleApiResponse.getResults() != null) {
                    for (GoogleApiResponse.Result place : googleApiResponse.getResults()) {
                        String key = place.getName() + "|" + place.getFormattedAddress();
                        if (savedNames.contains(key)) continue;

                        Restaurant.RestaurantBuilder builder = Restaurant.builder()
                                .name(place.getName())
                                .address(new Address(place.getFormattedAddress().substring(5)))
                                .latitude(place.getGeometry().getLocation().getLat())
                                .longitude(place.getGeometry().getLocation().getLng())
                                .score(place.getRating() * 2.0
                                        + Math.log(place.getUserRatingsTotal() + 1.0) * 3.0)
                                .avgRating(place.getRating());

                        // --- BEGIN: Save Google Place Photo (if available) ---
                        if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                            String photoReference = place.getPhotos().get(0).getPhotoReference();

                            try {
                                String apiUrl = "https://maps.googleapis.com/maps/api/place/photo"
                                        + "?maxwidth=800"
                                        + "&photo_reference=" + photoReference
                                        + "&key=" + "AIzaSyA1XCQwvYTPxSnM-PkSzSePWij3BwxZh7Q";

                                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(apiUrl).openConnection();
                                conn.setInstanceFollowRedirects(false);
                                conn.connect();

                                String redirectUrl = conn.getHeaderField("Location");

                                if (redirectUrl != null) {
                                    try (InputStream input = new URL(redirectUrl).openStream()) {
                                        byte[] bytes = input.readAllBytes();
                                        Cloudinary cloudinary = new Cloudinary(
                                                ObjectUtils.asMap(
                                                        "cloud_name", "dyzg8xb4n",
                                                        "api_key", "862213348176413",
                                                        "api_secret", "jTjOZjAq4KTEK0OXigQRv2kHOuE"
                                                )
                                        );
                                        Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
                                                "public_id", "restaurant_images/" + place.getPlaceId(),
                                                "overwrite", true
                                        ));

                                        String imageUrl = (String) uploadResult.get("secure_url");
                                        builder.imageUrl(imageUrl);
                                        System.out.println("Cloudinary 업로드 완료 URL: " + imageUrl);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Cloudinary 업로드 실패: " + e.getMessage());
                            }
                        }
                        // --- END: Save Google Place Photo (if available) ---

                        restaurantRepository.save(builder.build());
                        savedNames.add(key);
                    }
                }

                nextToken = googleApiResponse != null ? googleApiResponse.getNextPageToken() : null;
                isFirstRequest = false;
            }
        }
    }
}
