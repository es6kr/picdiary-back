package picdiary.diary.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import picdiary.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {
    IS_NOT_WRITER(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NO_DIARY(HttpStatus.BAD_REQUEST, "일기 정보가 존재하지 않습니다."),
    SAVE_ERROR(HttpStatus.BAD_REQUEST, "파일 저장 중 오류가 발생했습니다.");

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
