package com.sku.software.moacc.domain.user.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Role {
  @Schema(description = "사용자")
  USER,
  @Schema(description = "관리자")
  ADMIN,
  @Schema(description = "개발자")
  DEVELOPER

}
