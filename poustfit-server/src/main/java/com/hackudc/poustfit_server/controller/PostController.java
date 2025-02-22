package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.persistence.service.post.PostService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostDTO createPost() {
        return postService.createPost();
    }

    @PostMapping("/{id}/imagen")
    public void guardarImagen(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
        postService.savePostImage(id, file);
    }
}
