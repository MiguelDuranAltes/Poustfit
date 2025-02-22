package com.hackudc.poustfit_server.dto.in.post;

public class PostCreateDTO {
    private String title;

    public PostCreateDTO() {
    }

    public PostCreateDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
