package mjc.ramenlog.exception;

public class NotFoundRestaurantException extends BusinessException {
    public NotFoundRestaurantException() {
        super(ErrorCode.NOT_FOUND_RESTAURANT, "식당을 찾지 못했습니다");
    }
}
