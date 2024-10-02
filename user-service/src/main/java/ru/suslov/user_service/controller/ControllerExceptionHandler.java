package ru.suslov.user_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.suslov.user_service.exception.BadRegistrationDataException;
import ru.suslov.user_service.exception.TokenRefreshException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // todo UNAUTHORIZED
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<String> handleTokenRefreshException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // todo UNAUTHORIZED
    }

    @ExceptionHandler(BadRegistrationDataException.class)
    public ResponseEntity<String> handleBadRegistrationDataException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
