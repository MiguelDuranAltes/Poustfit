package com.hackudc.poustfit_server.persistence.repository;

import com.hackudc.poustfit_server.persistence.entity.user.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
}
