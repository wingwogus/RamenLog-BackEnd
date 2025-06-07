package mjc.ramenlog.controller;


import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.service.impl.GooglePlaceService;
import mjc.ramenlog.service.impl.KakaoPlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final KakaoPlaceService kakaoPlaceService;
    private final GooglePlaceService googlePlaceService;

    @PostMapping("/kakao")
    public ResponseEntity<?> importFromNaver(@RequestParam String keyword) {
        try{
            kakaoPlaceService.searchAndSaveRamenShops(keyword);
            return ResponseEntity.ok("가져오기 성공");
        }catch (Exception e){
            return ResponseEntity.status(500).body("에러발생 : " + e.getMessage());
        }
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<Void>> findAndSaveRestaurant() {
        googlePlaceService.findRestaurantAndSave();

        return ResponseEntity.ok(ApiResponse.success("저장 완료", null));
    }
}
