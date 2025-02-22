package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.in.auth.CredentialsDTO;
import com.hackudc.poustfit_server.dto.in.auth.UserPrivateDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserJwtDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserUsernameDTO;
import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.exceptions.login.CredentialsAreNotValidException;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;
import com.hackudc.poustfit_server.persistence.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final MyProperties myProperties;

    @Autowired
    public AuthController(AuthService authService, MyProperties myProperties) {
        this.authService = authService;
        this.myProperties = myProperties;
    }

    private void generateCookie(HttpServletResponse httpServletResponse, UserJwtDTO userJwtDTO) {
        Cookie cookie = new Cookie(myProperties.getJwtCookieName(), userJwtDTO.getJwt());
        cookie.setHttpOnly(true);  // Previene el acceso desde JavaScript en el navegador
        cookie.setSecure(false);    // Solo se envía a través de conexiones HTTPS
        cookie.setPath("/");       // Hace que la cookie esté disponible en toda la aplicación
        cookie.setMaxAge(24 * 60 * 60);  // Duración de la cookie (1 día en este caso)

        // Agrega la cookie a la respuesta
        httpServletResponse.addCookie(cookie);
    }

    @PostMapping("/register")
    public ResponseEntity<OkDTO> registerAccount(@RequestBody UserPrivateDTO account, HttpServletResponse httpServletResponse)
            throws UserUsernameExistsException, UserCorreoExistsException {

        UserJwtDTO userJwtDTO = authService.registerUser(account);

        generateCookie(httpServletResponse, userJwtDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new OkDTO("Usuario registrado correctamente"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<OkDTO> authenticate(@RequestBody CredentialsDTO credentials, HttpServletResponse httpServletResponse)
            throws CredentialsAreNotValidException {

        UserJwtDTO userJwtDTO = authService.login(credentials);

        generateCookie(httpServletResponse, userJwtDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new OkDTO("Usuario loggeado correctamente"));
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserUsernameDTO> getCurrentUser() {
        UserUsernameDTO userUsernameDTO = authService.getCurrentUser();
        return ResponseEntity.ok(userUsernameDTO);
    }
}
