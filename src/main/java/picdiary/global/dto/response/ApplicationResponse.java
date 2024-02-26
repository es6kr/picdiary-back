package picdiary.global.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApplicationResponse<T> {
    private T data;
    private String message;
    private String code;

    public ResponseEntity<ApplicationResponse<T>> entity() {
        return ResponseEntity.ok(this);
    }

    public static <T> ApplicationResponse<T> data(T data) {
        return data(data, null, null);
    }

    public static <T> ApplicationResponse<T> data(T data, String message, String code) {
        return new ApplicationResponse<>(data, message, code);
    }

    public static <V> ResponseEntity<ApplicationResponse<V>> error(String message, String code) {
        return ResponseEntity.internalServerError().body(new ApplicationResponse<>(null, message, code));
    }

    public static ApplicationResponse<Long> success(Long id, String message) {
        return data(id, message, null);
    }
}
