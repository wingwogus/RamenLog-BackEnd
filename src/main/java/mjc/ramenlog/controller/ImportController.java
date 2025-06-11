package mjc.ramenlog.controller;


import lombok.RequiredArgsConstructor;
import mjc.ramenlog.dto.ApiResponse;
import mjc.ramenlog.service.impl.GooglePlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final GooglePlaceService googlePlaceService;


    @PostMapping("/google")
    public ResponseEntity<ApiResponse<Void>> findAndSaveRestaurant() {
        googlePlaceService.findRestaurantAndSave();

        return ResponseEntity.ok(ApiResponse.success("저장 완료", null));
    }
}
