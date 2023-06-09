package com.truckhelper.application.exceptions;

public class AuthenticationError extends RuntimeException {
    public AuthenticationError() {
        super();
    }

    public AuthenticationError(Throwable cause) {
        super(cause);
    }
}
