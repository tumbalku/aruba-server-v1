package com.bahteramas.app.model.response;

import com.bahteramas.app.entity.CutiStatus;
import com.bahteramas.app.entity.CutiType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CutiResponse {

  private UserResponse user;
  private String type;
  private CutiStatus status;
  private String id;
  private String reason;
  private String message;
  private Boolean isRead;
  private LocalDate dateEnd;
  private LocalDate dateStart;
  private LocalDate confirmDate;
}

