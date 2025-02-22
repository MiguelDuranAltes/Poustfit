package com.hackudc.poustfit_server.persistence.service.auth;

import com.hackudc.poustfit_server.dto.in.auth.CredentialsDTO;
import com.hackudc.poustfit_server.dto.in.auth.UserPrivateDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserJwtDTO;
import com.hackudc.poustfit_server.dto.out.auth.UserUsernameDTO;
import com.hackudc.poustfit_server.exceptions.login.CredentialsAreNotValidException;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;

public interface AuthService {

    UserJwtDTO registerUser(UserPrivateDTO account) throws UserCorreoExistsException, UserUsernameExistsException;

    UserJwtDTO login(CredentialsDTO credentials) throws CredentialsAreNotValidException;

    UserUsernameDTO getCurrentUser();
}
