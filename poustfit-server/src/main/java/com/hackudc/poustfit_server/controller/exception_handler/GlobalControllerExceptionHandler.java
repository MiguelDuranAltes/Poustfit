package com.hackudc.poustfit_server.controller.exception_handler;

import com.hackudc.poustfit_server.exceptions.login.CredentialsAreNotValidException;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    //---------------------------------------------EXCEPCIONES DE REGISTRO----------------------------------------------
    @ExceptionHandler(UserCorreoExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserCorreoExistsException(UserCorreoExistsException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    @ExceptionHandler(UserUsernameExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserUsernameExistsException(UserUsernameExistsException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    //---------------------------------------------EXCEPCIONES DE LOGIN-------------------------------------------------
    @ExceptionHandler(CredentialsAreNotValidException.class)
    public ResponseEntity<ErrorDTO> handleCredentialsAreNotValidException(CredentialsAreNotValidException e) {
        System.out.println("ESTOY EN EL HANDLER DE CREDENTIALS");
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }
}
