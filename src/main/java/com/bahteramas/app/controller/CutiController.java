package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.CutiDecisionRequest;
import com.bahteramas.app.model.request.CutiDetailRequest;
import com.bahteramas.app.model.request.CutiRequest;
import com.bahteramas.app.model.response.CutiResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.service.impl.CutiServiceImpl;
import com.google.zxing.WriterException;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cuti")
@AllArgsConstructor
public class CutiController implements GenericController<String, CutiRequest, WebResponse<?>>{

  private final CutiServiceImpl cutiService;

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(User user,
                                             @PathVariable String id) throws IOException {

    Resource resource = cutiService.download(user, id);

    byte[] data = IOUtils.toByteArray(resource.getInputStream());


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "cuti-document.pdf");

    return new ResponseEntity<>(data, headers, HttpStatus.OK);
  }

  @PatchMapping("/decision/{id}")
  public WebResponse<CutiResponse> decision(User user, @PathVariable("id") String id,
                                            @RequestBody CutiDecisionRequest request){
    request.setId(id);
    CutiResponse response = cutiService.decision(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("The changes is saved!")
            .build();
  }

  @PostMapping("/make")
  public WebResponse<CutiResponse> makeCuti(User user, @RequestBody CutiRequest request)
          throws MalformedURLException, FileNotFoundException {
    CutiResponse response = cutiService.makeCuti(request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been created")
            .build();
  }
  @Override
  @PostMapping
  public WebResponse<CutiResponse> create(User user, @RequestBody CutiRequest request)
          throws MalformedURLException, FileNotFoundException {
    CutiResponse response = cutiService.create(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been requested, Please be patient!")
            .build();
  }

  @Override
  @PatchMapping("/{id}")
  public WebResponse<CutiResponse> update(User user, @PathVariable("id") String id,
                                          @RequestBody CutiRequest request) {
    request.setId(id);
    CutiResponse response = cutiService.update(user, request);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been updated!")
            .build();
  }

  @DeleteMapping("/file/{id}")
  public ResponseEntity<String> deleteFile(User user, @PathVariable String id) {
    boolean isDeleted = cutiService.deleteFile(id);
    cutiService.delete(user, id);
    if (isDeleted) {
      return ResponseEntity.ok("File deleted successfully");
    } else {
      return ResponseEntity.status(500).body("Failed to delete file");
    }
  }

  @Override
  @DeleteMapping("/{id}")
  public WebResponse<CutiResponse> delete(User user, @PathVariable("id") String id) {
    CutiResponse response = cutiService.delete(user, id);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Cuti has been deleted!")
            .build();
  }

  @Override
  @GetMapping
  public WebResponse<List<CutiResponse>> findAll() {
    List<CutiResponse> response = cutiService.findAll();
    return WebResponse.<List<CutiResponse>>builder()
            .data(response)
            .message("Success get all cuti")
            .build();
  }

  @GetMapping("/current")
  public WebResponse<List<CutiResponse>> findAllCurrentCuties(User user) {
    List<CutiResponse> response = cutiService.findAllCurrentCuties(user);
    return WebResponse.<List<CutiResponse>>builder()
            .data(response)
            .message("Success get all current cuti")
            .build();
  }

  @Override
  @GetMapping("/{id}")
  public WebResponse<CutiResponse> find(@PathVariable("id") String id) {
    CutiResponse response = cutiService.find(id);
    return WebResponse.<CutiResponse>builder()
            .data(response)
            .message("Success find cuti!")
            .build();
  }
}
