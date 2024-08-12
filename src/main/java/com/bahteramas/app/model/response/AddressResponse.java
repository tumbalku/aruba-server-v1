package com.bahteramas.app.model.response;

import com.bahteramas.app.entity.Gender;
import com.bahteramas.app.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

  private String name;
  private Long id;


}
