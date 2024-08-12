package com.bahteramas.app.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CivilServantResponse {
  private String rank;
  private String group;
  private String position;
  private String workUnit;

}
