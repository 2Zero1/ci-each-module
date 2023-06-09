package com.truckhelper.admin.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.truckhelper.admin.exceptions.AuthenticationError;

@ControllerAdvice
public class AuthenticationErrorAdvice {
    @ResponseBody
    @ExceptionHandler(AuthenticationError.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String authenticationError() {
        return "Authentication Error";
    }
}
