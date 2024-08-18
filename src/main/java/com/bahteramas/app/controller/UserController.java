package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.UserRequest;
import com.bahteramas.app.model.request.UserSearchRequest;
import com.bahteramas.app.model.response.UserResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.model.response.WebResponseWithPaging;
import com.bahteramas.app.service.impl.UserServiceImpl;
import com.bahteramas.app.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController implements GenericController<String, UserRequest, WebResponse<?>>{

  private final UserServiceImpl userService;

  @Override
  @PostMapping
  public WebResponse<UserResponse> create(User user, @RequestBody UserRequest request) {
    UserResponse response = userService.create(user, request);
    return WebResponse.<UserResponse>builder()
            .data(response)
            .message("Success create new user!")
            .build();
  }

  @Override
  @PatchMapping("/{id}")
  public WebResponse<UserResponse> update(User user, @PathVariable("id") String id, @RequestBody UserRequest request) {
    request.setId(id);
    UserResponse response = userService.update(user, request);
    return WebResponse.<UserResponse>builder()
            .data(response)
            .message("User has been updated")
            .build();
  }

  @Override
  @DeleteMapping("/{id}")
  public WebResponse<UserResponse> delete(User user, @PathVariable("id") String id) {
    UserResponse response = userService.delete(user, id);
    return WebResponse.<UserResponse>builder()
            .data(response)
            .message("User has been deleted")
            .build();
  }

  @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponseWithPaging<List<UserResponse>> search (User user,
                                                           @RequestParam(name = "identity", required = false) String identity,
                                                           @RequestParam(name = "status", required = false) String status,
                                                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                           @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    UserSearchRequest request = new UserSearchRequest();
    request.setIdentity(identity);
    request.setStatus(status);
    request.setPage(page);
    request.setSize(size);

    Page<UserResponse> responses = userService.searchUsers(request);

    return WebResponseWithPaging.<List<UserResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(Helper.getPagingResponse(responses))
            .build();
  }

  @Override
  @GetMapping
  public WebResponse<List<UserResponse>> findAll() {
    List<UserResponse> users = userService.findAll();
    return WebResponse.<List<UserResponse>>builder()
            .data(users)
            .message("Success get list of users")
            .build();
  }

  @Override
  @GetMapping("/{id}")
  public WebResponse<UserResponse> find(@PathVariable("id") String id) {
    UserResponse response = userService.find(id);
    return WebResponse.<UserResponse>builder()
            .data(response)
            .build();
  }


  @PatchMapping(path = "/avatar",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> updaterAvatar(User user,
                                           @RequestParam("avatar") MultipartFile file) throws IOException {

    userService.updaterAvatar(user, file);

    return WebResponse.<String>builder()
            .data("OK")
            .message("avatar has been updated")
            .build();
  }
}
