package com.hackudc.poustfit_server.persistence.service.auth;

import com.hackudc.poustfit_server.dto.in.auth.CredentialsDTO;
import com.hackudc.poustfit_server.dto.in.auth.UserPrivateDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserJwtDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserUsernameDTO;
import com.hackudc.poustfit_server.exceptions.login.CredentialsAreNotValidException;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import com.hackudc.poustfit_server.persistence.entity.user.JwtToken;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.persistence.repository.JwtTokenRepository;
import com.hackudc.poustfit_server.security.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, JwtTokenRepository jwtTokenRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }


    @Override
    @Transactional
    public UserJwtDTO registerUser(UserPrivateDTO account) throws UserCorreoExistsException, UserUsernameExistsException {

        System.out.println("----------Ejecutando: existsByCorreo en registerUser AuthServiceImpl----------");
        if (appUserRepository.existsByCorreo(account.getCorreo())) {
            throw new UserCorreoExistsException("Correo " + account.getCorreo() + " already exists");
        }

        System.out.println("----------Ejecutando: existsByUsername en registerUser AuthServiceImpl----------");
        if (appUserRepository.existsByUsername(account.getUsername())) {
            throw new UserUsernameExistsException("Username " + account.getUsername() + " already exists");
        }

        AppUser userToSave = new AppUser();
        String encryptedPassword = passwordEncoder.encode(account.getPassword());

        userToSave.setNombre(account.getNombre());
        userToSave.setApellidos(account.getApellidos());
        userToSave.setCorreo(account.getCorreo());
        userToSave.setUsername(account.getUsername());
        userToSave.setPassword(encryptedPassword);
        userToSave.setDescripcion(account.getDescripcion());

        // Genero un JWT para el usuario
        String jwt = jwtProvider.generateToken(account.getUsername(), "USER");
        // Hay que asociar el JWT al usuario
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(jwt);
        userToSave.setJwtToken(jwtToken);

        // Guardamos el usuario en la BD. Al hacer la propagacion del PERSIST, se guardara tambien el JWT
        System.out.println("----------Ejecutando: userRepo save en registerUser AuthServiceImpl----------");
        AppUser savedUser = appUserRepository.save(userToSave);

        UserJwtDTO userJwtDTO = new UserJwtDTO();
        userJwtDTO.setJwt(savedUser.getJwtToken().getToken());

        return userJwtDTO;
    }

    @Override
    @Transactional
    public UserJwtDTO login(CredentialsDTO credentials) throws CredentialsAreNotValidException {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword()
        );

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("----------Ejecutando: findByUsername en login AuthServiceImpl----------");
            Optional<AppUser> foundUser = appUserRepository.findByUsername(credentials.getUsername());

            // Siempre va a estar presente, ya que si no, hubiese dado una excepcion en el el authenticate()
            if (foundUser.isPresent()) {
                return resetJwtAndAssignNewOneToUser(foundUser.get(), foundUser.get().getJwtToken(), credentials.getUsername());
            } else {
                return null;
            }
        } catch (AuthenticationException e) {
            throw new CredentialsAreNotValidException(e.getMessage());
        }
    }

    @Override
    public UserUsernameDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserUsernameDTO userUsernameDTO = new UserUsernameDTO();
        userUsernameDTO.setUsername(username);
        return userUsernameDTO;
    }

    @Transactional
    protected UserJwtDTO resetJwtAndAssignNewOneToUser(AppUser userToUpdate, JwtToken lastToken, String username) {

        // Primero borramos el JWT anterior (tenemos que poner a null el jwt del usuario, ya que si no daria un
        // error de restriccion de clave foranea, ya que el propietario es el AppUser y el inverso es el JwtToken)
        userToUpdate.setJwtToken(null);
        if (lastToken != null) {
            System.out.println("----------Ejecutando: jwtRepo delete en resetJwtAndAssignNewOneToUser AuthServiceImpl----------");
            jwtTokenRepository.delete(lastToken);
        }

        // Genero un JWT para el usuario
        String jwt = jwtProvider.generateToken(username, "USER");
        // Hay que asociar el JWT al usuario
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(jwt);
        // Persistimos el token (lo persistimos y no hacemos como en registerUser porque en este caso se haría un merge)
        System.out.println("----------Ejecutando: jwtRepo save en resetJwtAndAssignNewOneToUser AuthServiceImpl----------");
        jwtTokenRepository.save(jwtToken);
        // Asociamos el token al usuario (como el usuario esta managed, no hace falta hacer save)
        System.out.println("----------Ejecutando: setJwtToken en resetJwtAndAssignNewOneToUser AuthServiceImpl----------");
        userToUpdate.setJwtToken(jwtToken);

        UserJwtDTO userJwtDTO = new UserJwtDTO();
        userJwtDTO.setJwt(userToUpdate.getJwtToken().getToken());

        return userJwtDTO;
    }
}
