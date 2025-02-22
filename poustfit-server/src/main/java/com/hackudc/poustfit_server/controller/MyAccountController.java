package com.hackudc.poustfit_server.controller;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.dto.out.post.PostDTO;
import com.hackudc.poustfit_server.dto.out.user.UserDTOPrivate;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.exceptions.NotFoundException;
import com.hackudc.poustfit_server.persistence.service.my_account.MyAccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/my-account")
public class MyAccountController {

    private final MyAccountService myAccountService;

    private final MyProperties myProperties;

    @Autowired
    public MyAccountController(MyAccountService myAccountService, MyProperties myProperties) {
        this.myAccountService = myAccountService;
        this.myProperties = myProperties;
    }

    @PostMapping("/sign-out")
    public ResponseEntity<OkDTO> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        myAccountService.logout(httpServletRequest);

        Cookie cookie = new Cookie(myProperties.getJwtCookieName(), "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new OkDTO("Sesión cerrada correctamente"));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTOPrivate> getMyInfo() {
        return ResponseEntity.ok(myAccountService.getMyInfo());
    }

    @GetMapping("/likes")
    public ResponseEntity<Page<PostDTO>> getMyLikedPosts(Pageable pageable) throws NotFoundException {
        Page<PostDTO> postDTOPage = myAccountService.getMyLikedPosts(pageable);
        return ResponseEntity.ok(postDTOPage);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> getMyPosts(Pageable pageable) throws NotFoundException {
        return ResponseEntity.ok(myAccountService.getMyPosts(pageable));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<OkDTO> saveUserImage(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
        myAccountService.saveUserImage(id, file);
        return ResponseEntity.ok(new OkDTO("Imagen guardada correctamente"));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<OkDTO> getUserImage(@PathVariable Long id,HttpServletResponse response) throws ModelException {
        ImageDTO imageDTO =myAccountService.getUserImage(id);

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
