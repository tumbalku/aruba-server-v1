package com.bahteramas.app.model.response;

import com.bahteramas.app.entity.DocType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {


  private Long id;
  private String filename;
  private DocType docType;
  private String fileType;
  private long fileSize;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;
}
