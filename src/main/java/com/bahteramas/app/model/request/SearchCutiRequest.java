package com.bahteramas.app.model.request;

import com.bahteramas.app.entity.CutiType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCutiRequest {

  private String name;
  private String type;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
