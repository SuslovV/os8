package ru.suslov.user_service.exception;

import org.springframework.security.core.AuthenticationException;

public class BadRegistrationDataException extends AuthenticationException {
    public BadRegistrationDataException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadRegistrationDataException(String msg) {
        super(msg);
    }
}
