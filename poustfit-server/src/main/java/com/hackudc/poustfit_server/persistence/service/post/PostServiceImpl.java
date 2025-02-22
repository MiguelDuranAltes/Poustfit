package com.hackudc.poustfit_server.persistence.service.post;

import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.MultimediaException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.PostRepository;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public PostDTO createPost(PostDTO newPost) {
        Post post = new Post();
        // Hay que hacer el post a Imgur y con eso obtengo la URL
        // post.setUrl_externa(newPost.getUrl_externa());
        post.setAutor(appUserRepository.findByUsername(newPost.getAutor()).get());
        postRepository.save(post);
        return new PostDTO(post);
    }

    @Override
    public PostDTO findPostById(Long id) {
        return new PostDTO(postRepository.findById(id).get());
    }

    @Transactional
    public void savePostImage(Long id, MultipartFile file) throws ModelException {
        Post post = postRepository.findById(id).get();
        if (post == null) {
            throw new NotFoundException(id.toString(), Post.class);
        }

        if (file.isEmpty()) {
            throw new ModelException("No se ha enviado ninguna imagen");
        }

        String nombreFichero = imageService.saveImage(file, id, true);
        post.setUrl_interna(nombreFichero);
        postRepository.save(post);
    }
}
