package exception;

import dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handleException(GlobalException exception) {
        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ExceptionResponse.from(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            MethodArgumentNotValidException exception
    ) {
        List<ExceptionResponse.ErrorField> errorFields = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ExceptionResponse.ErrorField(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ExceptionResponse.of(ApiErrorCode.INVALID_REQUEST, errorFields));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .badRequest()
                .body(ExceptionResponse.from(ApiErrorCode.INVALID_REQUEST));

    }
}