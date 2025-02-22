package com.hackudc.poustfit_server.persistence.service.post;

import com.hackudc.poustfit_server.dto.in.post.PostCreateDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    Page<PostDTO> findAll(Pageable pageable);

    PostDTO findById(Long id) throws NotFoundException;

    PostDTO createPost(PostCreateDTO postCreateDTO);

    void savePostImage(Long id, MultipartFile file) throws ModelException;

    void likeDislikePost(Long id, boolean like) throws ModelException;

    ImageDTO getPostImage(Long id) throws ModelException;
}
