package com.bahteramas.app.controller;

import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/image")
public class ImageController {

  private final ImageServiceImpl service;

  @Autowired
  public ImageController(ImageServiceImpl service) {
    this.service = service;
  }


  @GetMapping(path = "/{name}")
  public ResponseEntity<?> get(@PathVariable("name") String name){
    return ResponseEntity.status(HttpStatus.OK).body(service.getImageFromDb(name));
  }

  @DeleteMapping(path = "/{name}")
  public WebResponse<String> delete(@PathVariable("name") String name){
    service.removePrevImage(name);
    return WebResponse.<String>builder()
            .data("OK")
            .message("Image has been removed")
            .build();
  }
}
