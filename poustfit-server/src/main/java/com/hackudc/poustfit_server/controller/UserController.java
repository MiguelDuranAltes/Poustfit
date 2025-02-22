package com.hackudc.poustfit_server.controller;


import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPublic;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.persistence.service.app_user.AppUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;


@RestController
@RequestMapping("/users")
public class UserController {

    private final AppUserService appUserService;

    @Autowired
    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTOPublic> getUserInfo(@PathVariable String username) throws ModelException {
        return ResponseEntity.ok(appUserService.getUserInfo(username));
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable String username, Pageable pageable) throws ModelException {
        return ResponseEntity.ok(appUserService.getUserPosts(username, pageable));
    }

    @GetMapping("/{username}/image")
    public ResponseEntity<OkDTO> getUserImage(@PathVariable String username, HttpServletResponse response) throws ModelException {
        ImageDTO imageDTO = appUserService.getUserImage(username);

        try {
            response.setHeader("Content-disposition", "filename=" + imageDTO.getFilename());
            response.setContentType(imageDTO.getMimeType());
            IOUtils.copy(imageDTO.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new OkDTO("Imagen recuperada correctamente"));
    }
}
