package com.bahteramas.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String filename;

  @Enumerated(value = EnumType.STRING)
  private DocType docType;

  private String fileType;

  private long fileSize;

  @Lob
  @Column(name = "data", columnDefinition="LONGBLOB")
  private byte[] data;

  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;
}
