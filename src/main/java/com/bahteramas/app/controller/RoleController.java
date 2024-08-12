package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.RoleRequest;
import com.bahteramas.app.model.response.RoleResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.service.impl.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController implements GenericController<Integer, RoleRequest, WebResponse<?>>{

  private final RoleServiceImpl roleService;

  @Override
  @PostMapping
  public WebResponse<RoleResponse> create(User user, @RequestBody RoleRequest request) {
    RoleResponse response = roleService.create(user, request);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Success create new Role!")
            .build();
  }

  @Override
  @PatchMapping("/{id}")
  public WebResponse<RoleResponse> update(User user, @PathVariable("id") Integer id, @RequestBody RoleRequest request) {
    request.setId(id);
    RoleResponse response = roleService.update(user, request);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Role has been updated!")
            .build();
  }

  @Override
  @DeleteMapping("/{id}")
  public WebResponse<RoleResponse> delete(User user, @PathVariable("id") Integer id) {
    RoleResponse response = roleService.delete(user, id);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Role has been deleted!")
            .build();
  }

  @Override
  @GetMapping
  public WebResponse<List<RoleResponse>> findAll() {
    List<RoleResponse> response = roleService.findAll();
    return WebResponse.<List<RoleResponse>>builder()
            .data(response)
            .message("Success get roles")
            .build();
  }

  @Override
  @GetMapping("/{id}")
  public WebResponse<RoleResponse> find(@PathVariable("id") Integer id) {
    RoleResponse response = roleService.find(id);
    return WebResponse.<RoleResponse>builder()
            .data(response)
            .message("Success get role!")
            .build();
  }
}
