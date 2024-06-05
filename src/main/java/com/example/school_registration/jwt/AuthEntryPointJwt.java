package com.example.school_registration.jwt;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.info("REQUEST :  " + httpServletRequest.getPathInfo()  +" METHOD  : " + httpServletRequest.getMethod());
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        AuthError error = new AuthError();
        error.setErrors(authException.getMessage());
        error.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        String json = new Gson().toJson(error);
        response.getOutputStream().println(json);
    }

    @Getter
    static class AuthError{
        private String errors;
        private int code;

        public void setErrors(String errors) {
            this.errors = errors;
        }

        public void setCode(int code) {
            this.code = code;
        }

    }
}
