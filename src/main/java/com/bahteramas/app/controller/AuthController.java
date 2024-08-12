package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.LoginRequest;
import com.bahteramas.app.model.response.LoginResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Validated
public class AuthController {

  private final AuthServiceImpl authService;

  @Autowired
  public AuthController(AuthServiceImpl authService) {
    this.authService = authService;
  }


  @PostMapping(
          path = "/login",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<LoginResponse> loginUser(@RequestBody LoginRequest request){
    LoginResponse res = authService.loginUser(request);

    return WebResponse.<LoginResponse>builder()
            .data(res)
            .message("Logged in Successfully")
            .build();
  }

  @DeleteMapping(
          path = "/logout",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> logout(User user){
    authService.logout(user);
    return WebResponse.<String>builder()
            .data("OK")
            .message("Logged out Successfully")
            .build();
  }
}