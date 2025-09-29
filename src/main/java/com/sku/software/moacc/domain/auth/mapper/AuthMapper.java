package com.sku.software.moacc.domain.auth.mapper;

import com.sku.software.moacc.domain.auth.dto.response.LoginResponse;
import com.sku.software.moacc.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

  public LoginResponse toLoginResponse(User user, String accessToken, Long expirationTime) {
    return LoginResponse.builder()
        .accessToken(accessToken)
        .userId(user.getUserId())
        .username(user.getUsername())
        .role(user.getAuthRole())
        .expirationTime(expirationTime)
        .build();
  }
}
