package com.bahteramas.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
  String saveImageToDb(MultipartFile file) throws IOException;

  byte[] getImageFromDb(String name);

  void removePrevImage(String path);

}
