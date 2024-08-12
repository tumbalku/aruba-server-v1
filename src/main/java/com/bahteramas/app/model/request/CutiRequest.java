package com.bahteramas.app.model.request;

import com.bahteramas.app.entity.CutiType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CutiRequest {

  private String userNIP;
  private String id;
  private String type;

  @Lob
  private String reason;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  private String email;

  // tambahan
  private String name;
  private String unicode;
  private String num;
  private String romawi;
  private Boolean isPegawai;
  private List<String> tembusan;
  private String address;
}

