package io.github.vadman1.springbootwebfinaltask.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        log.error("Got validation exception", e);

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(
            NoSuchElementException e
    ) {
        log.error("Got exception", e);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorMessageResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleGenericException(
            Exception e
    ) {
        log.error("Server error", e);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessageResponse);
    }
}
