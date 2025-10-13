package com.sku.software.moacc.domain.user.mapper;

import com.sku.software.moacc.domain.user.dto.response.UserResponse;
import com.sku.software.moacc.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public static UserResponse toUserResponse(User user) {
    return UserResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .name(user.getNickname())
        .build();
  }

}
