package com.bahteramas.app.service;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.UserRequest;
import com.bahteramas.app.model.request.UserSearchRequest;
import com.bahteramas.app.model.response.SimpleUserResponse;
import com.bahteramas.app.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends GenericService<String, UserRequest, UserResponse> {

  User getUser(String id);
  Page<UserResponse> searchUsers(UserSearchRequest request);

  List<SimpleUserResponse> listSimpleUserResponse();

  void updaterAvatar(User user, MultipartFile file) throws IOException;
}
