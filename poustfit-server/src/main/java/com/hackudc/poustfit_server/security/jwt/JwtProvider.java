package com.hackudc.poustfit_server.security.jwt;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {

    private static final String AUTHORITIES_CLAIM_KEY = "auth";

    private final MyProperties myProperties;

    private final AppUserRepository appUserRepository;

    @Autowired
    public JwtProvider(MyProperties myProperties, AppUserRepository appUserRepository) {
        this.myProperties = myProperties;
        this.appUserRepository = appUserRepository;
    }

    // Metodo que genera un jwt
    public String generateToken(Authentication authentication) {
        String authority = authentication.getAuthorities().iterator().next().toString();

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + myProperties.getExpirationInMinutes() * 60 * 1000);
        SecretKey key = generateKey();

        return Jwts.builder()
                // Informacion del Header
                .header()
                .type("JWT")
                .and()
                // Informacion del Payload
                .subject(authentication.getName()) // authentication.getName() es el username (el Principal es del tipo
                                                   // User de UserDetails, pero te pilla directamente el username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claim(AUTHORITIES_CLAIM_KEY, authority)
                // Para firmar el token
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(String username, String authority) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + myProperties.getExpirationInMinutes() * 60 * 1000);
        SecretKey key = generateKey();

        return Jwts.builder()
                // Informacion del Header
                .header()
                .type("JWT")
                .and()
                // Informacion del Payload
                .subject(username) // authentication.getName() es el username (el Principal es el username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claim(AUTHORITIES_CLAIM_KEY, authority)
                // Para firmar el token
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // Metodo que valida el jwt
    @Transactional // Lo pongo porque creo que hay un lio y a veces da un error "org.hibernate.StaleObjectStateException: Row was updated or deleted by another transaction "
    public boolean validateToken(String jwt) {
        Claims claims = extractAllClaims(jwt);
        // Si no tira excepcion "extractAllClaims", es que el token es valido

        // Buscamos al usuario en la BD
        System.out.println("----------Ejecutando: userRepo findByUsername en JwtProvider----------");
        Optional<AppUser> foundUser = appUserRepository.findByUsername(claims.getSubject());
        if (foundUser.isEmpty() || foundUser.get().getJwtToken() == null) {
            return false;
        } else {
            // Comprobamos si el token que se ha enviado es el mismo que el que esta en la BD
            // (si no lo es, es que el usuario ha cerrado sesion y otro usuario ha cogido su token)
            System.out.println("----------Ejecutando: getJwtToken del JwtToken de AppUser en JwtProvider----------");
            return foundUser.get().getJwtToken().getToken().equals(jwt);
        }
    }

    // Metodo que devuelve un Authentication a partir de un jwt
    public Authentication getAuthentication(String jwt) {
        Claims claims = extractAllClaims(jwt);

        GrantedAuthority authority = new SimpleGrantedAuthority(claims.get(AUTHORITIES_CLAIM_KEY).toString());
        Collection<GrantedAuthority> authorities = Collections.singleton(authority);

        // Creamos un UserDetails con la implementacion de User (dados por Spring Security)
        // claims.getSubject() (el login del usuario) es el username
        // no le ponemos la contraseña
        // authorities es el rol USER (ya que por ahora no hay otros)
        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }

    // Metodo que extrae todos los claims del jwt
    private Claims extractAllClaims(String jwt) {
        SecretKey key = generateKey();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    // Metodo que genera una clave secreta a partir de la clave secreta del application.yml
    private SecretKey generateKey() {
        byte[] keyBytes = myProperties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
