package com.hackudc.poustfit_server.persistence.service.my_account;

import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import jakarta.servlet.http.HttpServletRequest;

public interface MyAccountService {

    void logout(HttpServletRequest httpServletRequest);

    public UserDTOPrivate getMyInfo();

}
