package picdiary.global.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ApplicationResponse<T> {
    private T data;
    private String message;
    private String code;

    public static <T> ResponseEntity<ApplicationResponse<T>> data(T data) {
        return data(data, null, null);
    }

    public static <T> ResponseEntity<ApplicationResponse<T>> data(T data, String message, String code) {
        return ResponseEntity.ok(new ApplicationResponse<>(data, message, code));
    }

    public static ResponseEntity<ApplicationResponse<Long>> success(Long id, String message) {
        return data(id, message, null);
    }
}
