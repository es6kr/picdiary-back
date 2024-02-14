package picdiary.diary.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import picdiary.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {
    NO_DIARY(HttpStatus.BAD_REQUEST, "일기 정보가 존재하지 않습니다."),
    EMPTY_DIARY(HttpStatus.BAD_REQUEST, "내용이 비어있습니다.");

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