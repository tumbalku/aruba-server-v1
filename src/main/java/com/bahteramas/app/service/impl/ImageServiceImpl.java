package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.Image;
import com.bahteramas.app.repository.ImageRepository;
import com.bahteramas.app.service.ImageService;
import com.bahteramas.app.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

  private final ImageRepository repository;
  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

  @Override
  @Transactional
  public String saveImageToDb(MultipartFile file) throws IOException {

    if(!Objects.equals(file.getContentType(), "image/png")
            && !Objects.equals(file.getContentType(), "image/jpeg")
            && !Objects.equals(file.getContentType(), "image/jpg")){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "only allow file with type [.png, .jpeg, .jpg]");
    }

    if(file.getSize() > MAX_FILE_SIZE){
      throw new ResponseStatusException(
              HttpStatus.PAYLOAD_TOO_LARGE,
              "Max file size should be 5MB");
    }

    repository.save(Image.builder()
            .path(Helper.nameConversion(file))
            .data(Helper.compressImage(file.getBytes()))
            .type(file.getContentType())
            .build());

    return Helper.nameConversion(file);
  }

  @Override
  @Transactional(readOnly = true)
  public byte[] getImageFromDb(String name) {

    Image image = findByPath(name);

    return Helper.decompressImage(image.getData());
  }

  private Image findByPath(String path){
    return repository.findByPath(path)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
  }
  @Override
  @Transactional
  public void removePrevImage(String path) {
    findByPath(path);
    repository.deleteByPath(path);
  }


}
