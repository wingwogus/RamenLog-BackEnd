package mjc.ramenlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.jpa.repository.Meta;

import java.util.List;

@Data
public class KakaoApiResponse {

    @JsonProperty("documents")
    private List<KakaoPlace> documents;

    @JsonProperty("meta")
    private Meta meta;

    @Data
    public static class Meta{
        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;
    }
}
