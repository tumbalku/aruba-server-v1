package com.bahteramas.app.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDocRequest {

  private String filename;
  private String docType;

  private String fileType;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
