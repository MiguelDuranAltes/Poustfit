package com.hackudc.poustfit_server.security.config;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public MyUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        // Como no tengo rol de administrador, le pongo el rol USER por defecto a todos los usuarios
        // Si se quisiera implementar roles, hay que poner un atributo en la entidad AppUser
        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
        return new User(username, appUser.get().getPassword(), Collections.singleton(authority));
    }
}
