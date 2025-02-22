package com.hackudc.poustfit_server.security.exception_handler;

import com.hackudc.poustfit_server.config.MyProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final MyProperties myProperties;

    @Autowired
    public MyAccessDeniedHandler(MyProperties myProperties) {
        this.myProperties = myProperties;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("Access denied");
        System.out.println(accessDeniedException.getMessage());

        // Hacemos que la cookie se borre
        Cookie cookie = new Cookie(myProperties.getJwtCookieName(), "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
