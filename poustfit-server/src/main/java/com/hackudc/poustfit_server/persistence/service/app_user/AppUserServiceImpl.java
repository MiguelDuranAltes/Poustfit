package com.hackudc.poustfit_server.persistence.service.app_user;

import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPublic;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final ImageService imageService;

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, ImageService imageService) {
        this.appUserRepository = appUserRepository;
        this.imageService = imageService;
    }


    @Override
    public UserDTOPublic getUserInfo(String username) throws ModelException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if(user.isPresent()){
            return new UserDTOPublic(user.get());
        }else{
            throw new NotFoundException("User with username " + username + " not found",AppUser.class);
        }
    }

    @Override
    @Transactional
    public Page<PostDTO> getUserPosts(String username,Pageable pageable) throws NotFoundException {

        AppUser foundUser = appUserRepository.findByUsername(username).get();

        Page<Post> postPage = appUserRepository.findPostsByUser(foundUser.getId(), pageable);

        Page<PostDTO> postDTOPage = postPage.map(post -> new PostDTO(post, appUserRepository.likesPost(foundUser.getId(), post.getId())));

        return postDTOPage;

    }

    @Override
    public ImageDTO getUserImage(String username) throws ModelException {
        AppUser user = appUserRepository.findByUsername(username).get();
        if (user == null) {
            throw new NotFoundException("User not found", AppUser.class);
        }

        return imageService.getImage(user.getId(), user.getImageName(), false);
    }
}
