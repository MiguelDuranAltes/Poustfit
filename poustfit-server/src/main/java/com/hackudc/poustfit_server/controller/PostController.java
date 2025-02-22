package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.persistence.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody String descripcion) {
        PostDTO postDTO = postService.createPost(descripcion);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<OkDTO> guardarImagen(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
        postService.savePostImage(id, file);
        return ResponseEntity.ok(new OkDTO("Imagen guardada correctamente"));
    }

    @PostMapping("/{id}/like")
    public void likePost(@PathVariable Long id) throws ModelException {
        postService.likeDislikePost(id, true);
    }
    @PostMapping("/{id}/disLike")
    public void disLikePost(@PathVariable Long id) throws ModelException {
        postService.likeDislikePost(id, false);
    }
}
