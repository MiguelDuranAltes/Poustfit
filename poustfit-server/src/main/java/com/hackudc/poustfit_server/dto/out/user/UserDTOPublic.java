package com.hackudc.poustfit_server.dto.out.user;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
public class UserDTOPublic {

    private Long id;
    private String username;
    private boolean hasImage = false;

    public UserDTOPublic() {
    }

    public UserDTOPublic(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
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

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
