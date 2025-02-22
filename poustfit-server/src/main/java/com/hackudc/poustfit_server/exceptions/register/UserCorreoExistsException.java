package com.hackudc.poustfit_server.exceptions.register;

public class UserCorreoExistsException extends Exception {
    public UserCorreoExistsException(String message) {
        super(message);
    }
}
