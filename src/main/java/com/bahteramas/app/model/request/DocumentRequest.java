package com.bahteramas.app.model.request;

import com.bahteramas.app.entity.DocType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {

  @JsonIgnore
  private Long id;

  private String filename;
  private String docType;
}
