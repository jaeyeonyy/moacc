package com.sku.software.moacc.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "UserResponse DTO", description = "사용자 정보 응답")
public class UserResponse {

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "사용자 아이디", example = "jaeyeon20@gmail.com")
  private String username;

  @Schema(description = "사용자 이름", example = "재연")
  private String name;

}
