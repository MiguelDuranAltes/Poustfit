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
import com.hackudc.poustfit_server.persistence.service.image.ImageServiceFileSystem;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MyAccountServiceImpl implements MyAccountService {

    private final AppUserRepository appUserRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final MyProperties myProperties;

    @Autowired
    private ImageService imageService;

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
        List<Post> posts = user.getPostsLiked();
        return posts.stream().map(PostDTO::new)  // Convierte cada Post en un PostDTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveUserImage(Long id, MultipartFile file) throws ModelException {
        AppUser user = appUserRepository.getById(id);
        if (user == null) {
            throw new NotFoundException(id.toString(), AppUser.class);
        }

        if (file.isEmpty()) {
            throw new ModelException("No se ha seleccionado ningún archivo");
        }

        String imageName = imageService.saveImage(file, id, false);
        user.setImageName(imageName);
        appUserRepository.save(user);
    }


    public ImageDTO getUserImage(Long id) throws ModelException {
        AppUser user = appUserRepository.getById(id);
        if (user == null) {
            throw new NotFoundException(id.toString(), AppUser.class);
        }

        return imageService.getImage(id, user.getImageName(), false);
    }

}
