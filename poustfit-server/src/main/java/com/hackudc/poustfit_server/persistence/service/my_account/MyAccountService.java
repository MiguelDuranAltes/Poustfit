package com.hackudc.poustfit_server.persistence.service.my_account;

import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MyAccountService {

    void logout(HttpServletRequest httpServletRequest);

    public UserDTOPrivate getMyInfo();

    public List<PostDTO> getMyLikedPosts() throws NotFoundException;

    public List<PostDTO> getMyPosts() throws NotFoundException;

}
