package com.hackudc.poustfit_server.config;

import com.hackudc.poustfit_server.dto.in.auth.UserPrivateDTO;
import com.hackudc.poustfit_server.exceptions.OperationNotAllowed;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;
import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.PostRepository;
import com.hackudc.poustfit_server.persistence.service.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseLoader {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthService authService;

    @Autowired

    private PasswordEncoder passwordEncoder;


    @Autowired
    private PostRepository postRepository;

    @Transactional
    public void loadData() throws UserUsernameExistsException,UserCorreoExistsException {

        authService.registerUser(new UserPrivateDTO("Lucas", "Redondo Puga", "LucasRedon", "lucas@udc.es",
                passwordEncoder.encode("password123"), "Este es el perfil de Lucas" ));

        authService.registerUser(new UserPrivateDTO("Juan", "Pérez", "JuanPerez", "juanito@udc.es",
                passwordEncoder.encode("password123"), "Este es el perfil de Juan" ));


    }

}
