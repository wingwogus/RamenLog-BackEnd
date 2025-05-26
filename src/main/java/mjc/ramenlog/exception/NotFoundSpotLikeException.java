package mjc.ramenlog.exception;

public class NotFoundSpotLikeException extends BusinessException {
    public NotFoundSpotLikeException() {
        super(ErrorCode.NOT_FOUND_SPOTLIKE, "좋아요를 찾지 못했습니다");
    }
}
