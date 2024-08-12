package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.AddressRequest;
import com.bahteramas.app.model.response.AddressResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.service.impl.AddressServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@AllArgsConstructor
public class AddressController implements GenericController<Long, AddressRequest, WebResponse<?>>{

  private final AddressServiceImpl addressService;

  @Override
  @PostMapping
  public WebResponse<AddressResponse> create(User user, @RequestBody AddressRequest request) {
    AddressResponse response = addressService.create(user, request);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Success create new Address!")
            .build();
  }

  @Override
  @PatchMapping("/{id}")
  public WebResponse<AddressResponse> update(User user, @PathVariable("id") Long id, @RequestBody AddressRequest request) {
    request.setId(id);
    AddressResponse response = addressService.update(user, request);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Address has been updated!")
            .build();
  }

  @Override
  @DeleteMapping("/{id}")
  public WebResponse<AddressResponse> delete(User user, @PathVariable("id") Long id) {
    AddressResponse response = addressService.delete(user, id);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Address has been deleted!")
            .build();
  }

  @Override
  @GetMapping
  public WebResponse<List<AddressResponse>> findAll() {
    List<AddressResponse> response = addressService.findAll();
    return WebResponse.<List<AddressResponse>>builder()
            .data(response)
            .message("Success get addresses")
            .build();
  }

  @Override
  @GetMapping("/{id}")
  public WebResponse<AddressResponse> find(@PathVariable("id") Long id) {
    AddressResponse response = addressService.find(id);
    return WebResponse.<AddressResponse>builder()
            .data(response)
            .message("Success get role!")
            .build();
  }
}
