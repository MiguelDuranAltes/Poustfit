package com.hackudc.poustfit_server.dto.out.user;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
public class UserDTOPublic {

    private String username;
    private boolean hasImage = false;

    private String description;

    public UserDTOPublic() {
    }

    public UserDTOPublic(AppUser user) {
        this.username = user.getUsername();
        this.description = user.getDescripcion();
        this.hasImage = user.getImageName() != null;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
