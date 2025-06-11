package mjc.ramenlog.service.inf;

import org.springframework.web.multipart.MultipartFile;

public interface ReviewFileStorageService {
    String storeReviewImage(MultipartFile file);
}
