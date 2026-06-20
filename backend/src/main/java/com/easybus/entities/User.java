package com.easybus.entities;

import com.easybus.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fullName;

  private String username;

  private String avatarUrl;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @Size(max = 40)
  @Column(unique = true)
  private String email;

  private Boolean emailVerified;

  private String locale;

  @Column(unique = true)
  private String googleSub;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role = Role.USER;
}
