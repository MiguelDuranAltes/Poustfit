package com.hackudc.poustfit_server.persistence.service.image;

import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.exceptions.MultimediaException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  String saveImage(MultipartFile file, Long id, boolean post) throws MultimediaException;

  ImageDTO getImage(Long id, String nombreImagen, boolean post) throws MultimediaException;

  void deleteImage(Long id, String nombreImagen, boolean post) throws MultimediaException;

  ImageDTO getProductImage(String nombreImagen) throws MultimediaException;
}
