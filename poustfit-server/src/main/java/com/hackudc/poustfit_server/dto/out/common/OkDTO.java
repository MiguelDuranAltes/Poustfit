package com.hackudc.poustfit_server.dto.out.common;

public class OkDTO {

    private String message;

    public OkDTO() {
    }

    public OkDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
