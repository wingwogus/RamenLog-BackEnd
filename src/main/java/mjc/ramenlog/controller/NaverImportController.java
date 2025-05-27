package mjc.ramenlog.controller;


import lombok.RequiredArgsConstructor;
import mjc.ramenlog.service.impl.NaverPlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NaverImportController {

    private final NaverPlaceService naverPlaceService;

    @PostMapping("/api/import/naver")
    public ResponseEntity<?> importFromNaver(@RequestParam String keyword) {
        try{
            naverPlaceService.searchAndSaveRamenShops(keyword);
            return ResponseEntity.ok("가져오기 성공");
        }catch (Exception e){
            return ResponseEntity.status(500).body("에러발생 : " + e.getMessage());
        }
    }
}
