package com.truckhelper.application.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class BackdoorControlInterceptor implements HandlerInterceptor {
    @Value("${backdoor.active}")
    private boolean active;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String path = request.getRequestURI();

        if (!path.startsWith("/backdoor/")) {
            return true;
        }

        return active;
    }
}
