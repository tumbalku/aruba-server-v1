package com.bahteramas.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String path;

  @Lob
  @Column(name = "data", columnDefinition="LONGBLOB")
  private byte[] data;

  private String type;

}
