package mjc.ramenlog.exception;

public class NotVerifiedEmailException extends BusinessException {
    public NotVerifiedEmailException(String message) {
        super(ErrorCode.NOT_VERIFIED_EMAIL, message);
    }
}
