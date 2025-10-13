package com.sku.software.moacc.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;import com.sku.software.moacc.domain.user.enums.Role;
import com.sku.software.moacc.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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
  private Long id;

  // 로그인 식별자: username으로 변경 (unique)
  @Column(nullable = false, unique = true)
  private String username; // 로그인 ID

  // 별도의 이메일 필드 추가
  @Column
  private String email; // 이메일 (로그인 식별자 아님)

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname; // 닉네임

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