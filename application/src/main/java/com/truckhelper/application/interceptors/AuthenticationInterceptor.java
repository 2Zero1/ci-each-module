package com.truckhelper.application.interceptors;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.truckhelper.application.exceptions.AuthenticationError;
import com.truckhelper.application.repositories.UserRepository;
import com.truckhelper.application.utils.JwtUtil;
import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(UserRepository userRepository,
                                     JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return true;
        }

        String accessToken = authorization.substring("Bearer ".length());

        try {
            UserId userId = jwtUtil.decode(accessToken);
            Optional<User> currentUser = userRepository.findById(userId);

            request.setAttribute(
                    "userId", currentUser.isPresent() ? userId : null);
            request.setAttribute("currentUser", currentUser);

            return true;
        } catch (JWTDecodeException | SignatureVerificationException e) {
            throw new AuthenticationError(e);
        }
    }
}
