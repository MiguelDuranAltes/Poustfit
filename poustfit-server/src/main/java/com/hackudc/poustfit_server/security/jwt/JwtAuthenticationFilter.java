package com.hackudc.poustfit_server.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.controller.exception_handler.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MyProperties myProperties;

    private final JwtProvider jwtProvider;

    // Rutas públicas
    private  final Set<String> PUBLIC_URLS = Set.of(
            "/auth/authenticate",
            "/auth/register",
            "/my-account/sign-out"
    );

    @Autowired
    public JwtAuthenticationFilter(MyProperties myProperties, JwtProvider jwtProvider) {
        this.myProperties = myProperties;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Peticion: " + request.getMethod() + " a " +  request.getServletPath() + "-------------------------------------------------------------------------------------");

        String jwt = resolveToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Necesario por si se quiere acceder a una ruta pública y ademas tiene un token en la cookie
        // Es por si dicho token es invalido o expirado y que pueda entrar en la ruta pública
        if (PUBLIC_URLS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtProvider.validateToken(jwt)) {
                Authentication authentication = jwtProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException eje) {
            System.out.println("Security exception for user " + eje.getClaims().getSubject() + " - " + eje.getMessage());
            generateResponse(response, "La sesión ha expirado, vuelva a iniciar sesión");
        } catch (SignatureException eje) {
            System.out.println("Signature exception " + eje.getMessage());
            generateResponse(response, "La firma del token no es válida, vuelva a iniciar sesión");
        } catch (UnsupportedJwtException eje) {
            System.out.println("Unsupported JWT exception " + eje.getMessage());
            generateResponse(response, "Error desconocido, vuelva a iniciar sesión");

        } catch (MalformedJwtException eje) {
            System.out.println("Malformed JWT exception " + eje.getMessage());
            generateResponse(response, "El token no es válido, vuelva a iniciar sesión");

        }
    }

    private String resolveToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Buscar la cookie que te interesa por su nombre
                if (myProperties.getJwtCookieName().equals(cookie.getName())) {
                    // Obtener el valor de la cookie
                    return cookie.getValue();
                }
            }
        } else {
            // No se encontraron cookies
            return null;
        }

        // No se encontro la cookie necesaria
        return null;
    }

    private void generateResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorDTO errorResponse = new ErrorDTO(message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
