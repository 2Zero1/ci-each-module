package com.truckhelper.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.truckhelper.application.interceptors.AuthenticationInterceptor;
import com.truckhelper.application.interceptors.BackdoorControlInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BackdoorControlInterceptor backdoorControlInterceptor;

    private final AuthenticationInterceptor authenticationInterceptor;

    public WebMvcConfig(BackdoorControlInterceptor backdoorControlInterceptor,
                        AuthenticationInterceptor authenticationInterceptor) {
        this.backdoorControlInterceptor = backdoorControlInterceptor;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(backdoorControlInterceptor);
        registry.addInterceptor(authenticationInterceptor);
    }
}
