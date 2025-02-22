package com.hackudc.poustfit_server.dto.out.user;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;

public class UserDTOPrivate {

    private Long id;
    private String nombre;
    private String apellidos;
    private String username;
    private String correo;
    private String descripcion;
    private boolean hasImage = false;

    public UserDTOPrivate() {
    }

    public UserDTOPrivate(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nombre = user.getNombre();
        this.apellidos = user.getApellidos();
        this.correo = user.getCorreo();
        this.descripcion = user.getDescripcion();
        this.hasImage = user.getImageName() != null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
