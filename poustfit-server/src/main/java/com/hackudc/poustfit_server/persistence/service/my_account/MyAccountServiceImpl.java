package com.hackudc.poustfit_server.persistence.service.my_account;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.entity.user.JwtToken;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.JwtTokenRepository;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyAccountServiceImpl implements MyAccountService {

    private final AppUserRepository appUserRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final MyProperties myProperties;

    @Autowired
    public MyAccountServiceImpl(AppUserRepository appUserRepository, JwtTokenRepository jwtTokenRepository, MyProperties myProperties) {
        this.appUserRepository = appUserRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.myProperties = myProperties;
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
    public List<PostDTO> getMyLikedPosts() throws NotFoundException {
        String username = SecurityUtils.getCurrentUserLogin();
        Optional<AppUser> foundUser = appUserRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            throw new NotFoundException(username, AppUser.class);
        }
        AppUser user = foundUser.get();
        List<Post> postsLiked = user.getPostsLiked();
        return postsLiked.stream().map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostDTO> getMyPosts() throws NotFoundException {
        String username = SecurityUtils.getCurrentUserLogin();
        Optional<AppUser> foundUser = appUserRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            throw new NotFoundException(username, AppUser.class);
        }
        AppUser user = foundUser.get();
        List<Post> posts = user.getPosts();
        return posts.stream().map(PostDTO::new)
                .collect(Collectors.toList());

    }
}
