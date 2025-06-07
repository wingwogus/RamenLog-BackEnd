package mjc.ramenlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class GoogleApiResponse {

    @JsonProperty("next_page_token")
    private String nextPageToken;

    @JsonProperty("results")
    private List<Result> results;

    @Data
    public static class Result {
        @JsonProperty("name")
        private String name;

        @JsonProperty("formatted_address")
        private String formattedAddress;

        @JsonProperty("rating")
        private double rating;

        @JsonProperty("user_ratings_total")
        private int userRatingsTotal;

        @JsonProperty("photos")
        private List<Photo> photos;

        @JsonProperty("place_id")
        private String placeId;

        @JsonProperty("geometry")
        private Geometry geometry;

        @Getter
        public static class Photo {
            @JsonProperty("photo_reference")
            private String photoReference;
        }

        @Data
        public static class Geometry {
            @JsonProperty("location")
            private Location location;

            @Getter
            public static class Location {
                @JsonProperty("lat")
                private double lat;

                @JsonProperty("lng")
                private double lng;

            }
        }
    }
}
