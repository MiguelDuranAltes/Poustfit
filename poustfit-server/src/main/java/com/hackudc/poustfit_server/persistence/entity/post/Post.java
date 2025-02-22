package com.hackudc.poustfit_server.persistence.entity.post;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime fechaPublicacion = LocalDateTime.now().withNano(0);

    @Column
    private String url_interna;

    @Column
    private String url_externa;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private AppUser autor;

    @ManyToMany(mappedBy = "postsLiked", fetch = FetchType.LAZY)
    private List<AppUser> likedUsers = new ArrayList<>();

    @Column
    private String title;

    public Post() {
    }

    public Post(String title, String url_interna, String url_externa, AppUser autor) {
        this.title = title;
        this.url_interna = url_interna;
        this.url_externa = url_externa;
        this.autor = autor;
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

    public String getUrl_interna() {
        return url_interna;
    }

    public void setUrl_interna(String url_interna) {
        this.url_interna = url_interna;
    }

    public String getUrl_externa() {
        return url_externa;
    }

    public void setUrl_externa(String url_externa) {
        this.url_externa = url_externa;
    }

    public AppUser getAutor() {
        return autor;
    }

    public void setAutor(AppUser autor) {
        this.autor = autor;
    }

    public  List<AppUser> getLikedUsers() { return likedUsers; }
    public void setLikedUsers(List<AppUser> likedUsers) { this.likedUsers = likedUsers; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getId(), post.getId()) && Objects.equals(getUrl_externa(), post.getUrl_externa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUrl_externa());
    }
}
