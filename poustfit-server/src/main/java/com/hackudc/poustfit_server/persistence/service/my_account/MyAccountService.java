package com.hackudc.poustfit_server.persistence.service.my_account;

import jakarta.servlet.http.HttpServletRequest;

public interface MyAccountService {

    void logout(HttpServletRequest httpServletRequest);
}
