package com.hackudc.poustfit_server.persistence.repository;

import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    Optional<AppUser> findByJwtToken_Token(String jwtToken);

}
