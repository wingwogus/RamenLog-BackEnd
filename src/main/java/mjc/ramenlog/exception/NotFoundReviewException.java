package mjc.ramenlog.exception;

public class NotFoundReviewException extends BusinessException {
    public NotFoundReviewException() {
        super(ErrorCode.NOT_FOUND_REVIEW, "리뷰를 찾을 수 없습니다.");
    }
}
