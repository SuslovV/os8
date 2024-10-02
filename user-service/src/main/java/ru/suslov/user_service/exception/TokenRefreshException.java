package ru.suslov.user_service.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenRefreshException extends AuthenticationException {
    public TokenRefreshException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenRefreshException(String msg) {
        super(msg);
    }
}
