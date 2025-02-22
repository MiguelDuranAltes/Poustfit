package com.hackudc.poustfit_server.persistence.service.post;

import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.PostRepository;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import com.hackudc.poustfit_server.remote.imgur.ApiClientImgur;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ApiClientImgur imgurClient;

    @Override
    @Transactional
    public PostDTO createPost() {
        Post post = new Post();
        String username = SecurityUtils.getCurrentUserLogin();
        post.setAutor(appUserRepository.findByUsername(username).get());
        postRepository.save(post);
        return new PostDTO(post);
    }

    @Override
    public PostDTO findPostById(Long id) {
        return new PostDTO(postRepository.findById(id).get());
    }

    @Override
    @Transactional
    public void savePostImage(Long id, MultipartFile file) throws ModelException{
        Post post = postRepository.findById(id).get();
        if (post == null) {
            throw new NotFoundException(id.toString(), Post.class);
        }

        if (file.isEmpty()) {
            throw new ModelException("No se ha enviado ninguna imagen");
        }

        String imageUrl = imgurClient.uploadImage(file);
        post.setUrl_externa(imageUrl);

        String nombreFichero = imageService.saveImage(file, id, true);
        post.setUrl_interna(nombreFichero);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void likeDislikePost(Long id, boolean like) throws ModelException {
        //Encuentro el post
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isEmpty()) {
            throw new NotFoundException(id.toString(), Post.class);
        }
        Post post = foundPost.get();

        String username = SecurityUtils.getCurrentUserLogin();

        //Encuentro el usuario
        Optional<AppUser> foundUser = appUserRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            throw new NotFoundException(username, AppUser.class);
        }
        AppUser user = foundUser.get();

        //Añado o quito la publicación de la lista de likes del usuario
        if (like) {
            user.getPostsLiked().add(post);
        } else {
            user.getPostsLiked().remove(post);
        }

    }
}
