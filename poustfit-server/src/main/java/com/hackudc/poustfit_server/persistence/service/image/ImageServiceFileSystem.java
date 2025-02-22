package com.hackudc.poustfit_server.persistence.service.image;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.exceptions.MultimediaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceFileSystem implements ImageService {

  @Autowired
  private MyProperties properties;

  private Path rootLocation;


  public String saveImage(MultipartFile file, Long id, boolean post) throws MultimediaException {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());

    try (InputStream is = file.getInputStream()) {
      Files.copy(is, getRootLocation(post).resolve(id + getExtension(filename)), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      throw new MultimediaException("Se ha producido algún error procesando la imagen");
    }
    return filename;
  }

  private String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf("."));
  }

  private Path getRootLocation(boolean post) {
    if(post){
      this.rootLocation = Paths.get(properties.getMediaPathPost());
    }
    else{
      this.rootLocation = Paths.get(properties.getMediaPathUser());
    }
    return this.rootLocation;
  }

  public ImageDTO getImage(Long id, String nombreImagen, boolean post) throws MultimediaException {
    try {
      String ruta;
      if(post){
        ruta = properties.getMediaPathPost();
      }else
        ruta= properties.getMediaPathUser();

      InputStream is = new FileInputStream(ruta + id + getExtension(nombreImagen));

      byte[] buffer = new byte[1024];
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      int len;
      while ((len = is.read(buffer)) > -1) {
        os.write(buffer, 0, len);
      }
      InputStream imageIs = new ByteArrayInputStream(os.toByteArray());
      os.flush();
      is.close();

      return new ImageDTO(imageIs, nombreImagen, getImageMediaType(nombreImagen));
    } catch (IOException e) {
      e.printStackTrace();
      throw new MultimediaException("se ha producido algún error al recuperar la imagen");
    }
  }

  private String getImageMediaType(String nombreImagen) {
    String extension = getExtension(nombreImagen);
    switch (extension.toLowerCase()) {
      case ".png":
        return MediaType.IMAGE_PNG_VALUE;
      case ".gif":
        return MediaType.IMAGE_GIF_VALUE;
      case ".jpg":
      case ".jpeg":
      default:
        return MediaType.IMAGE_JPEG_VALUE;
    }
  }

  public void deleteImage(Long id, String nombreImagen, boolean post) throws MultimediaException {
    try {
      Files.deleteIfExists(getRootLocation(post).resolve(id + getExtension(nombreImagen)));
    } catch (IOException e) {
      e.printStackTrace();
      throw new MultimediaException("Se ha producido algún error al borrar la imagen");
    }
  }
}
