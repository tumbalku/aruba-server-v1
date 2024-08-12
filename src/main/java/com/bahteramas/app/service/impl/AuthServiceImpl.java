package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.LoginRequest;
import com.bahteramas.app.model.response.LoginResponse;
import com.bahteramas.app.repository.UserRepository;
import com.bahteramas.app.security.BCrypt;
import com.bahteramas.app.service.AuthService;
import com.bahteramas.app.utils.Helper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private Helper helper;
  @Override
  @Transactional
  public LoginResponse loginUser(LoginRequest request) {

    helper.validate(request);

    User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This username maybe not exist"));
    if(BCrypt.checkpw(request.getPassword(), user.getPassword())){

      // success login
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(System.currentTimeMillis() + (36L * 1_00_000 * 24 * 30));
      userRepository.save(user);

      return LoginResponse.builder()
              .token(user.getToken())
              .tokenExpiredAt(user.getTokenExpiredAt())
              .user(Helper.userToResponse(user))
              .build();
    }else{
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You send wrong password!");
    }
  }

  @Override
  @Transactional
  public void logout(User user){
    user.setTokenExpiredAt(null);
    user.setToken(null);

    userRepository.save(user);
  }
}
