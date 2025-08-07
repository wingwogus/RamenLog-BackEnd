package mjc.ramenlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoogleDetailsResponse {
    private Result result;

    @Data
    public static class Result {
        private String name;

        @JsonProperty("formatted_address")
        private String formattedAddress;

        @JsonProperty("formatted_phone_number")
        private String formattedPhoneNumber;

        private Double rating;

        @JsonProperty("user_ratings_total")
        private Integer userRatingsTotal;

        private Geometry geometry;

        @JsonProperty("opening_hours")
        private OpeningHours openingHours;

        @Data
        public static class Geometry {
            private Location location;

            @Data
            public static class Location {
                private Double lat;
                private Double lng;
            }
        }

        @Data
        public static class OpeningHours {
            @JsonProperty("open_now")
            private Boolean openNow;

            @JsonProperty("weekday_text")
            private List<String> weekdayText;
        }
    }
}