package picdiary.todo.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import picdiary.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {
    IS_NOT_WRITER(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
    NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
    @Override
    public HttpStatus getStatusCode() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
