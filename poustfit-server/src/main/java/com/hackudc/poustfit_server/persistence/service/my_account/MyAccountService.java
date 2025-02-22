package com.hackudc.poustfit_server.persistence.service.my_account;

import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MyAccountService {

    void logout(HttpServletRequest httpServletRequest);

    public UserDTOPrivate getMyInfo();

    public List<PostDTO> getMyLikedPosts() throws NotFoundException;

    public List<PostDTO> getMyPosts() throws NotFoundException;

    public void saveUserImage(Long id,MultipartFile file) throws ModelException;

    public ImageDTO getUserImage(Long id) throws ModelException;

}
