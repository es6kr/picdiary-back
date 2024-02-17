package picdiary.global.exception;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import picdiary.global.dto.response.ApplicationResponse;

@ControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationResponse<Object>> internalError(ApplicationException e) {
        var code = e.getErrorCode();
        var status = code.getStatusCode();
        var body = ApplicationResponse.builder().code(status.name()).message(code.getMessage()).build();
        return ResponseEntity.status(status.value()).body(body);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class})
    public ApplicationResponse<Object> notFound(ServletException e) {
        return ApplicationResponse.builder().code(HttpStatus.NOT_FOUND.name()).message(e.getMessage()).build();
    }
}
