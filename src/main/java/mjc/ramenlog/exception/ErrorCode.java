package mjc.ramenlog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN"),
    NOT_VERIFIED_EMAIL(HttpStatus.FORBIDDEN, "NOT_VERIFIED_EMAIL"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "DUPLICATE_NICKNAME"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOT_FOUND_MEMBER"),
    NOT_FOUND_SPOTLIKE(HttpStatus.NOT_FOUND, "NOT_FOUND_SPOTLIKE" ),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR"),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "NOT_FOUND_RESTAURANT" ),
    ALREADY_SPOTLIKE(HttpStatus.CONFLICT, "ALREADY_SPOTLIKE" ),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "NOT_FOUND_REVIEW"),;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus staus, String internalError) {
        this.status = staus;
        this.message = internalError;
    }
}