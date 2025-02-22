package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.dto.in.post.PostCreateDTO;
import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.service.post.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> findAll(Pageable pageable) {
        Page<PostDTO> postDTOPage = postService.findAll(pageable);
        return ResponseEntity.ok(postDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) throws NotFoundException {
        PostDTO postDTO = postService.findById(id);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        PostDTO postDTO = postService.createPost(postCreateDTO);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<OkDTO> guardarImagen(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
        postService.savePostImage(id, file);
        return ResponseEntity.ok(new OkDTO("Imagen guardada correctamente"));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<OkDTO> likePost(@PathVariable Long id) throws ModelException {
        postService.likeDislikePost(id, true);
        return ResponseEntity.ok(new OkDTO("Post liked"));
    }

    @PostMapping("/{id}/disLike")
    public ResponseEntity<OkDTO> disLikePost(@PathVariable Long id) throws ModelException {
        postService.likeDislikePost(id, false);
        return ResponseEntity.ok(new OkDTO("Post disliked"));
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<OkDTO> getImage(@PathVariable Long id, @RequestParam HttpServletResponse response) throws ModelException {
        ImageDTO imageDTO=postService.getPostImage(id);

        try {
            response.setHeader("Content-disposition", "filename=" + imageDTO.getFilename());
            response.setContentType(imageDTO.getMimeType());
            IOUtils.copy(imageDTO.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new OkDTO("Imagen recuperada correctamente"));

    }
}
