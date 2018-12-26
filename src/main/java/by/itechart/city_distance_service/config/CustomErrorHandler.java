package by.itechart.city_distance_service.config;

import by.itechart.city_distance_service.exception.CityNotFoundException;
import by.itechart.city_distance_service.exception.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class })
    public final ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
        log(exception);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({ CityNotFoundException.class, PathNotFoundException.class})
    public final ResponseEntity<Object> handleNotFoundException(final RuntimeException exception) {
        log(exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex);
        return new ResponseEntity<>("Invalid arguments", headers, status);
    }

    @ExceptionHandler({ Exception.class })
    public final ResponseEntity<Object> handleAnyOtherException(final Exception exception) {
        log(exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    private void log(final Exception exception) {
        logger.error(exception.getMessage(), exception);
    }
}
