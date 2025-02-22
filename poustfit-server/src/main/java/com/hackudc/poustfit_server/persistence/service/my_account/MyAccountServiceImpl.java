package com.hackudc.poustfit_server.persistence.service.my_account;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.entity.user.JwtToken;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.JwtTokenRepository;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MyAccountServiceImpl implements MyAccountService {

    private final AppUserRepository appUserRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final MyProperties myProperties;

    private final ImageService imageService;

    @Autowired
    public MyAccountServiceImpl(AppUserRepository appUserRepository, JwtTokenRepository jwtTokenRepository, MyProperties myProperties, ImageService imageService) {
        this.appUserRepository = appUserRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.myProperties = myProperties;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String jwt = "some_token";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Buscar la cookie que te interesa por su nombre
                if (myProperties.getJwtCookieName().equals(cookie.getName())) {
                    jwt = cookie.getValue();
                }
            }
        } else {
            return;
        }

        System.out.println("----------Ejecutando: findByJwtToken_Token en logout AppUserServiceImp----------");
        Optional<AppUser> foundUser = appUserRepository.findByJwtToken_Token(jwt);

        if (foundUser.isPresent()) {
            JwtToken lastToken = foundUser.get().getJwtToken();
            // Primero ponemos su token a null, para que no de error de integridad referencial
            foundUser.get().setJwtToken(null);
            // Borramos el token de la bd
            System.out.println("----------Ejecutando: delete en logout AppUserServiceImp----------");
            jwtTokenRepository.delete(lastToken);
        }
    }
    @Override
    public UserDTOPrivate getMyInfo(){

        String user= SecurityUtils.getCurrentUserLogin();
        if(user!=null){
            return new UserDTOPrivate(appUserRepository.findByUsername(user).get());
        }
        return null;

    }

    @Override
    @Transactional
    public Page<PostDTO> getMyLikedPosts(Pageable pageable) throws NotFoundException {

        String username = SecurityUtils.getCurrentUserLogin();

        AppUser foundUser = appUserRepository.findByUsername(username).get();

        Page<Post> postPage = appUserRepository.findPostsLikedByUser(foundUser.getId(), pageable);

        Page<PostDTO> postDTOPage = postPage.map(post -> new PostDTO(post, appUserRepository.likesPost(foundUser.getId(), post.getId())));

        return postDTOPage;

    }
    @Override
    @Transactional
    public Page<PostDTO> getMyPosts(Pageable pageable) throws NotFoundException {
        String username = SecurityUtils.getCurrentUserLogin();
        AppUser foundUser = appUserRepository.findByUsername(username).get();

        Page<Post> postPage = appUserRepository.findPostsByUser(foundUser.getId(), pageable);

        Page<PostDTO> postDTOPage = postPage.map(post -> new PostDTO(post, appUserRepository.likesPost(foundUser.getId(), post.getId())));

        return postDTOPage;

    }


    @Override
    @Transactional
    public void saveUserImage(MultipartFile file) throws ModelException {
        String username = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByUsername(username).get();
        if (user == null) {
            throw new NotFoundException("User not found", AppUser.class);
        }

        if (file.isEmpty()) {
            throw new ModelException("No se ha seleccionado ningún archivo");
        }

        String imageName = imageService.saveImage(file, user.getId(), false);
        user.setImageName(imageName);
        appUserRepository.save(user);
    }

    @Override
    public ImageDTO getUserImage() throws ModelException {
        String username = SecurityUtils.getCurrentUserLogin();
        AppUser user = appUserRepository.findByUsername(username).get();
        if (user == null) {
            throw new NotFoundException("User not found", AppUser.class);
        }

        return imageService.getImage(user.getId(), user.getImageName(), false);
    }

}
