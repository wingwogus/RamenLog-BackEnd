package mjc.ramenlog.service.impl;

import lombok.RequiredArgsConstructor;
import mjc.ramenlog.service.inf.ReviewFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewFileStorageServiceImpl implements ReviewFileStorageService {

    @Value("${app.upload.dir:uploads/reviews}")
    private String uploadDir;

    @Override
    public String storeReviewImage(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" +
                    StringUtils.cleanPath(file.getOriginalFilename());
            Path target = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(target);
            Path copy = target.resolve(filename);
            file.transferTo(copy.toFile());
            return "/uploads/reviews/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("리뷰 이미지 저장 실패", e);
        }
    }
}
