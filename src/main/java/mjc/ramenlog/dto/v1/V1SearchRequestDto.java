package mjc.ramenlog.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * Places API (v1) searchText 요청 본문을 위한 DTO.
 * pageToken이 있을 경우 textQuery는 전송하지 않도록 설정합니다.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // 직렬화 시 null인 필드는 제외
public class V1SearchRequestDto {
    private String textQuery;
    private String pageToken;
    private final String languageCode = "ko"; // 언어는 한국어로 고정

    public V1SearchRequestDto(String textQuery, String pageToken) {
        // 다음 페이지 요청 시에는 textQuery가 null이 되어야 합니다.
        this.textQuery = (pageToken == null) ? textQuery : null;
        this.pageToken = pageToken;
    }
}