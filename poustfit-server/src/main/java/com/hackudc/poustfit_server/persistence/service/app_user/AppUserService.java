package com.hackudc.poustfit_server.persistence.service.app_user;

import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPublic;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AppUserService {


    public UserDTOPublic getUserInfo(String username) throws ModelException;

    public Page<PostDTO> getUserPosts(String username, Pageable pageable) throws NotFoundException;

    public ImageDTO getUserImage(String username) throws ModelException;
}
