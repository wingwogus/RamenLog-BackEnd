package mjc.ramenlog.exception;

public class VerificationFailedException extends BusinessException {

    public VerificationFailedException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }
}