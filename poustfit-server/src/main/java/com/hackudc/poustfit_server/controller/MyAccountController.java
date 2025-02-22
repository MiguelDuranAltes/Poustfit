package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.persistence.service.my_account.MyAccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my-account")
public class MyAccountController {

    private final MyAccountService myAccountService;

    private final MyProperties myProperties;

    @Autowired
    public MyAccountController(MyAccountService myAccountService, MyProperties myProperties) {
        this.myAccountService = myAccountService;
        this.myProperties = myProperties;
    }

    @PostMapping("/sign-out")
    public ResponseEntity<OkDTO> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        myAccountService.logout(httpServletRequest);

        Cookie cookie = new Cookie(myProperties.getJwtCookieName(), "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new OkDTO("Sesión cerrada correctamente"));
    }
}
