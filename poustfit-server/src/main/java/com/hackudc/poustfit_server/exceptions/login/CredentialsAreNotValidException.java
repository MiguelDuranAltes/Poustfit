package com.hackudc.poustfit_server.exceptions.login;

public class CredentialsAreNotValidException extends RuntimeException {
    public CredentialsAreNotValidException(String message) {
        super(message);
    }
}
