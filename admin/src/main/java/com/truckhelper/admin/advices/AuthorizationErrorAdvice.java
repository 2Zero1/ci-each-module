package com.truckhelper.admin.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.truckhelper.admin.exceptions.AuthorizationError;

@ControllerAdvice
public class AuthorizationErrorAdvice {
    @ResponseBody
    @ExceptionHandler(AuthorizationError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String authorizationError() {
        return "Authorization Error";
    }
}
