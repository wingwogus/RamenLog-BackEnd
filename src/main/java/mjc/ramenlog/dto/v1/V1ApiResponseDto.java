package mjc.ramenlog.dto.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Places API (v1) searchText의 최상위 응답을 위한 DTO
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class V1ApiResponseDto {
    private List<V1PlaceDto> places;

    @JsonProperty("nextPageToken")
    private String nextPageToken;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class V1PlaceDto {
        private String id;
        private DisplayName displayName;
        private String formattedAddress;
        private Double rating;
        private Integer userRatingCount;
        private Location location;
        private RegularOpeningHours regularOpeningHours;
        private CurrentOpeningHours currentOpeningHours;
        private List<Photo> photos;
        private String nationalPhoneNumber;
        private List<Review> reviews;

        // ⚠️ 리뷰 객체를 위한 내부 클래스 추가
        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Review {
            private List<Photo> photos;
            // 필요하다면 text, rating 등 다른 리뷰 필드도 추가 가능
        }

        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DisplayName {
            private String text;
        }

        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location {
            private Double latitude;
            private Double longitude;
        }

        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RegularOpeningHours {
            private List<String> weekdayDescriptions;
        }

        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CurrentOpeningHours {
            private Boolean openNow;
        }

        @Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Photo {
            // "places/{place_id}/photos/{photo_reference}" 형식의 전체 이름
            private String name;
        }
    }
}

