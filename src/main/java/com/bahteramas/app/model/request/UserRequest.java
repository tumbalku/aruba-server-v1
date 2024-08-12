package com.bahteramas.app.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  private String id;
  private String name;
  private String username;
  private String email;
  private String gender;
  private String phone;
  private String address;
  private List<String> roles;
  private String status;
  private CivilServantRequest cs;

}
