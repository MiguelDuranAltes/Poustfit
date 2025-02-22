package com.hackudc.poustfit_server.persistence.service.post;

import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostDTO createPost(PostDTO newPost);

    PostDTO findPostById(Long id);

    void savePostImage(Long id, MultipartFile file) throws ModelException;
}
