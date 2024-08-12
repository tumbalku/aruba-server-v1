package com.bahteramas.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cuti")
public class Cuti {
  @Id
  private String id;

  @ManyToOne
  @JoinColumn(name = "username")
  private User user;

  @Enumerated(EnumType.STRING)
  private CutiType type;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String reason;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  private Boolean isRead;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  private CutiStatus status;

  @Lob
  private String message;
  private LocalDate confirmDate;
}
