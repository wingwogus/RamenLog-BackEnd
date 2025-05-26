package mjc.ramenlog.exception;

public class AlreadySpotLikeException extends BusinessException {

    public AlreadySpotLikeException() {
        super(ErrorCode.ALREADY_SPOTLIKE, "이미 좋아요를 눌렀습니다");
    }
}
