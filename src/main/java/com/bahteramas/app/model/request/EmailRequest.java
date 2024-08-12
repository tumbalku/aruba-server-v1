package com.bahteramas.app.model.request;


import com.bahteramas.app.entity.CutiType;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

  @Lob
  private String reason;
  private String name;
  private String nip;
  private String token;
  private CutiType type;
  private String to;
}
