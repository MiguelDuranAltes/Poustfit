package com.hackudc.poustfit_server.persistence.service.post;

import com.hackudc.poustfit_server.dto.in.post.PostCreateDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.PostRepository;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import com.hackudc.poustfit_server.remote.imgbb.ApiClientImgbb;
import com.hackudc.poustfit_server.remote.imgur.ApiClientImgur;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final AppUserRepository appUserRepository;

    private final PostRepository postRepository;

    private final ImageService imageService;


    private final ApiClientImgbb imgbbClient;

    @Autowired
    public PostServiceImpl(AppUserRepository appUserRepository, PostRepository postRepository, ImageService imageService, ApiClientImgbb imgbbClient) {
        this.appUserRepository = appUserRepository;
        this.postRepository = postRepository;
        this.imageService = imageService;
        this.imgbbClient = imgbbClient;
    }

    @Override
    @Transactional
    public Page<PostDTO> findAll(Pageable pageable) {

        String username = SecurityUtils.getCurrentUserLogin();

        AppUser user = appUserRepository.findByUsername(username).get();

        Page<Post> postPage = postRepository.findAll(pageable);

        Page<PostDTO> postDTOPage = postPage.map(post -> new PostDTO(post, appUserRepository.likesPost(user.getId(), post.getId())));

        return postDTOPage;
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
        String username = SecurityUtils.getCurrentUserLogin();
        post.setAutor(appUserRepository.findByUsername(username).get());
        postRepository.save(post);
        return new PostDTO(post, false);
    }

    @Override
    @Transactional
    public void savePostImage(Long id, MultipartFile file) throws ModelException{
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new NotFoundException(id.toString(), Post.class);
        }

        if (file.isEmpty()) {
            throw new ModelException("No se ha enviado ninguna imagen");
        }
        Post post = postOptional.get();
        String imageUrl = imgbbClient.uploadImage(file);
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

    @Override
    public ImageDTO getPostImage(Long id) throws ModelException {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new NotFoundException(id.toString(), Post.class);
        }
        Post post = postOptional.get();
        return imageService.getImage(id, post.getUrl_interna(), true);
    }
}
