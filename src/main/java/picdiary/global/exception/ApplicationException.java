package picdiary.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import picdiary.global.exception.ErrorCode;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
