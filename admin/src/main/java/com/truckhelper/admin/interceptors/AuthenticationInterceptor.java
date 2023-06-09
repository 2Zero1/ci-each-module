package com.truckhelper.admin.interceptors;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.admin.utils.JwtUtil;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AdminRepository adminRepository;

    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(AdminRepository adminRepository,
                                     JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
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
            AdminId adminId = jwtUtil.decode(accessToken);
            Optional<Admin> currentAdmin = adminRepository.findById(adminId);

            request.setAttribute(
                    "adminId", currentAdmin.isPresent() ? adminId : null);
            request.setAttribute("currentAdmin", currentAdmin);

            return true;
        } catch (JWTDecodeException | SignatureVerificationException e) {
            throw new AuthenticationError(e);
        }
    }
}
