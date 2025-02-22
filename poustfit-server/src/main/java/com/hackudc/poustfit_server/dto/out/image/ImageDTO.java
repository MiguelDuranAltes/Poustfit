package com.hackudc.poustfit_server.dto.out.image;

import java.io.InputStream;

public class ImageDTO {
  private final InputStream inputStream;
  private final String filename;
  private final String mimeType;

  public ImageDTO(InputStream inputStream, String filename, String mimeType) {
    super();
    this.inputStream = inputStream;
    this.filename = filename;
    this.mimeType = mimeType;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public String getFilename() {
    return filename;
  }

  public String getMimeType() {
    return mimeType;
  }

}
