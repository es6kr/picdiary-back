package picdiary.user.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import picdiary.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

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