package com.bahteramas.app.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CutiDecisionRequest {

  @JsonIgnore
  private String id;
  private String status;
  private String message;
}
