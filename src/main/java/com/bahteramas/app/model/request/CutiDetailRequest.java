package com.bahteramas.app.model.request;

import com.bahteramas.app.entity.CutiType;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutiDetailRequest {

  private String name;
  private String unicode;
  private String num;
  private String romawi;
  private boolean isPegawai;
  private List<String> tembusan;
  private CutiType cutiType;
  private String address;

}
