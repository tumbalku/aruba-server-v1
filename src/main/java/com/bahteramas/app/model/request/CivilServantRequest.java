package com.bahteramas.app.model.request;


import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CivilServantRequest {
  private String rank;
  private String group;
  private String position;
  private String workUnit;

}
