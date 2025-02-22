package com.hackudc.poustfit_server.persistence.service.app_user;

import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.persistence.repository.AppUserRepository;
import com.hackudc.poustfit_server.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDTOPrivate getMyInfo(){

        String user= SecurityUtils.getCurrentUserLogin();
        if(user!=null){
            return new UserDTOPrivate(appUserRepository.findByUsername(user).get());
        }
        return null;

    }
}
