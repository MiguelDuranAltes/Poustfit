package com.hackudc.poustfit_server.exceptions.register;

public class UserUsernameExistsException extends Exception {
    public UserUsernameExistsException(String message) {
        super(message);
    }
}
