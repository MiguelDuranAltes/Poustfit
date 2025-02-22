package com.hackudc.poustfit_server.dto.out.post;

import com.hackudc.poustfit_server.persistence.entity.post.Post;
import org.springframework.security.access.method.P;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;
    private LocalDateTime fechaPublicacion;
    private String url_externa;
    private String autor;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.fechaPublicacion = post.getFechaPublicacion();
        this.url_externa = post.getUrl_externa();
        this.autor = post.getAutor().getUsername();
    }

    //----------------------------------------------GETTERS Y SETTERS---------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getUrl_externa() {
        return url_externa;
    }

    public void setUrl_externa(String url_externa) {
        this.url_externa = url_externa;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }


}
