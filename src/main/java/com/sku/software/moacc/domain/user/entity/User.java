package com.sku.software.moacc.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sku.software.moacc.domain.user.enums.Language;
import com.sku.software.moacc.domain.user.enums.Role;
import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username; // 이메일

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role authRole;

  @JsonIgnore
  @Column(name = "refresh_token")
  private String refreshToken;


  public void createRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
