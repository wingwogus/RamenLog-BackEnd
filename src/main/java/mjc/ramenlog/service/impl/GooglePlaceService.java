package mjc.ramenlog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mjc.ramenlog.domain.Address;
import mjc.ramenlog.domain.Restaurant;
import mjc.ramenlog.dto.v1.V1ApiResponseDto;
import mjc.ramenlog.dto.v1.V1SearchRequestDto;
import mjc.ramenlog.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlaceService {
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;

    @Value("${google.key}")
    private String googleApiKey;
    @Value("${cloudinary.key}")
    private String cloudinaryKey;
    @Value("${cloudinary.secret}")
    private String cloudinarySecret;
    @Value("${cloudinary.name}")
    private String cloudinaryName;

    @Transactional
    public void findRestaurantAndSave() {
        // 라멘집이 있을 확률이 높은 주요 도시 및 지역 목록으로 수정
        List<String> regions = Arrays.asList(
                // 서울특별시 (전체)
                "서울 중구", "서울 종로구", "서울 용산구", "서울 성동구", "서울 광진구", "서울 동대문구", "서울 중랑구", "서울 성북구", "서울 강북구", "서울 도봉구", "서울 노원구", "서울 은평구", "서울 서대문구", "서울 마포구", "서울 양천구", "서울 강서구", "서울 구로구", "서울 금천구", "서울 영등포구", "서울 동작구", "서울 관악구", "서울 서초구", "서울 강남구", "서울 송파구", "서울 강동구",
                // 6대 광역시 (주요 구)
                "부산 부산진구", "부산 해운대구", "부산 남구", "부산 동래구", "부산 수영구", "대구 중구", "대구 수성구", "인천 부평구", "인천 남동구", "인천 연수구", "광주 동구", "광주 서구", "대전 서구", "대전 유성구", "울산 남구", "울산 중구", "세종시",
                // 경기도 (주요 시)
                "수원시 팔달구", "수원시 영통구", "성남시 분당구", "고양시 일산동구", "고양시 일산서구", "용인시 수지구", "용인시 기흥구", "부천시", "안양시 동안구", "안산시 단원구", "화성시", "남양주시", "평택시", "의정부시", "파주시", "시흥시", "김포시",
                // 기타 주요 도시
                "춘천시", "원주시", "강릉시", "청주시", "충주시", "천안시", "아산시", "전주시", "군산시", "익산시", "목포시", "여수시", "순천시", "포항시", "구미시", "경주시", "경산시", "창원시 성산구", "김해시", "양산시", "진주시", "거제시", "제주시", "서귀포시"
        );

        Set<String> savedPlaceIds = ConcurrentHashMap.newKeySet();

        for (String region : regions) {
            String nextPageToken = null;

            do {
                try {
                    String url = "https://places.googleapis.com/v1/places:searchText";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("X-Goog-Api-Key", googleApiKey);
                    headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.rating,places.userRatingCount,places.location,places.regularOpeningHours,places.currentOpeningHours,places.photos,places.nationalPhoneNumber,places.reviews,nextPageToken");

                    V1SearchRequestDto requestBody = new V1SearchRequestDto(region + " 라멘", nextPageToken);
                    HttpEntity<V1SearchRequestDto> entity = new HttpEntity<>(requestBody, headers);

                    ResponseEntity<V1ApiResponseDto> response = restTemplate.exchange(
                            url, HttpMethod.POST, entity, V1ApiResponseDto.class);

                    V1ApiResponseDto apiResponse = response.getBody();

                    if (apiResponse != null && apiResponse.getPlaces() != null) {
                        for (V1ApiResponseDto.V1PlaceDto place : apiResponse.getPlaces()) {
                            String placeId = place.getId().replace("places/", "");
                            if (savedPlaceIds.contains(placeId) || restaurantRepository.existsByPlaceId(placeId)) {
                                continue;
                            }

                            Address address = new Address(place.getFormattedAddress());

                            double score = 0.0;
                            if (place.getRating() != null && place.getUserRatingCount() != null) {
                                score = place.getRating() * 2.0 + Math.log(place.getUserRatingCount() + 1.0) * 3.0;
                            }

                            Restaurant.RestaurantBuilder builder = Restaurant.builder()
                                    .placeId(placeId)
                                    .name(place.getDisplayName() != null ? place.getDisplayName().getText() : null)
                                    .address(address)
                                    .latitude(place.getLocation() != null ? place.getLocation().getLatitude() : 0.0)
                                    .longitude(place.getLocation() != null ? place.getLocation().getLongitude() : 0.0)
                                    .score(score)
                                    .avgRating(place.getRating())
                                    .phoneNumber(place.getNationalPhoneNumber())
                                    .openNow(place.getCurrentOpeningHours() != null ? place.getCurrentOpeningHours().getOpenNow() : null)
                                    .weekdayText(place.getRegularOpeningHours() != null ? place.getRegularOpeningHours().getWeekdayDescriptions() : null);

                            // ⚠️ 헬퍼 메서드를 호출하여 최적의 사진 레퍼런스를 찾습니다.
                            String photoReference = findBestPhotoReference(place);

                            if (photoReference != null) {
                                try {
                                    String imageUrl = uploadPhotoToCloudinary(photoReference, placeId);
                                    builder.imageUrl(imageUrl);
                                } catch (Exception e) {
                                    log.error("Cloudinary upload failed for placeId: {}", placeId, e);
                                }
                            }

                            restaurantRepository.save(builder.build());
                            savedPlaceIds.add(placeId);
                        }
                    }

                    nextPageToken = (apiResponse != null) ? apiResponse.getNextPageToken() : null;

                    if (StringUtils.hasText(nextPageToken)) {
                        Thread.sleep(2000);
                    }

                } catch (Exception e) {
                    log.error("Error while fetching data for region: {}", region, e);
                    nextPageToken = null;
                }
            } while (StringUtils.hasText(nextPageToken));
        }
        log.info("Finished all regions.");
    }

    /**
     * ⚠️ 리뷰 사진을 우선으로, 없으면 가게 대표 사진을 반환하는 헬퍼 메서드
     * @param place V1PlaceDto 객체
     * @return 사진 레퍼런스 문자열 또는 null
     */
    private String findBestPhotoReference(V1ApiResponseDto.V1PlaceDto place) {
        // 1. 리뷰에 사진이 있는지 먼저 확인
        if (place.getReviews() != null && !place.getReviews().isEmpty()) {
            for (V1ApiResponseDto.V1PlaceDto.Review review : place.getReviews()) {
                if (review.getPhotos() != null && !review.getPhotos().isEmpty()) {
                    String photoName = review.getPhotos().get(0).getName();
                    // photoName 형식: "places/{place_id}/photos/{photo_reference}"
                    return photoName.substring(photoName.lastIndexOf('/') + 1);
                }
            }
        }
        // 2. 리뷰에 사진이 없으면, 장소의 대표 사진을 사용 (Fallback)
        if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
            String photoName = place.getPhotos().get(0).getName();
            return photoName.substring(photoName.lastIndexOf('/') + 1);
        }
        // 3. 어떤 사진도 없으면 null 반환
        return null;
    }

    private String uploadPhotoToCloudinary(String photoReference, String placeId) throws Exception {
        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + photoReference + "&key=" + googleApiKey;

        byte[] imageBytes = restTemplate.getForObject(photoUrl, byte[].class);

        if (imageBytes == null) {
            throw new RuntimeException("Failed to download image from Google.");
        }

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryName,
                "api_key", cloudinaryKey,
                "api_secret", cloudinarySecret));

        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                "public_id", "restaurant_images/" + placeId,
                "overwrite", true
        ));

        String secureUrl = (String) uploadResult.get("secure_url");
        log.info("Cloudinary upload complete. URL: {}", secureUrl);
        return secureUrl;
    }
}