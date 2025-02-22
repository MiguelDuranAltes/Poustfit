package com.hackudc.poustfit_server.security.exception_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.controller.exception_handler.ErrorDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyUnauthorizedEntryPointHandler implements AuthenticationEntryPoint {

    private final MyProperties myProperties;

    @Autowired
    public MyUnauthorizedEntryPointHandler(MyProperties myProperties) {
        this.myProperties = myProperties;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        System.out.println("Unauthorized Entry");
        System.out.println(authException.getMessage());

        // Hacemos que la cookie se borre
        Cookie cookie = new Cookie(myProperties.getJwtCookieName(), "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorDTO errorResponse = new ErrorDTO("Para realizar esta operación necesita estar autenticado, vuelva a iniciar sesión");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
