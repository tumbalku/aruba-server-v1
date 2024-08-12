package com.bahteramas.app.service;

import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.LoginRequest;
import com.bahteramas.app.model.response.LoginResponse;

public interface AuthService {
LoginResponse loginUser(LoginRequest request);
void logout(User user);
}
