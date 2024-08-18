package com.bahteramas.app.model.response;

import com.bahteramas.app.entity.Gender;
import com.bahteramas.app.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private String id;
  private String name;
  private String avatar;
  private String email;
  private String phone;
  private Gender gender;
  private String address;
  private UserStatus status;
  private CivilServantResponse civilServant;
  private List<String> roles;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;



}
