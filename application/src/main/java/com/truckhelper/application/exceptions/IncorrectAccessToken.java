package com.truckhelper.application.exceptions;

public class IncorrectAccessToken extends RuntimeException {
    public IncorrectAccessToken() {
        super();
    }

    public IncorrectAccessToken(Throwable cause) {
        super(cause);
    }
}
