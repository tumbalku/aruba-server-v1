package com.bahteramas.app.entity;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  private String id;

  private String name;
  private Integer cutiTotal;
  private String token;
  private Long tokenExpiredAt;

  private String avatar;
  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles = new HashSet<>();

//  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address", referencedColumnName = "id")
  private Address address;

  @Column(nullable = false, unique = true)
  private String phone;

  @Enumerated(value = EnumType.STRING)
  private UserStatus status;

  @Embedded
  private CivilServant civilServant;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;


}
