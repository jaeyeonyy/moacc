package com.sku.software.moacc.domain.user.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  @Schema(description = "사용자")
  USER("사용자"),
  @Schema(description = "관리자")
  ADMIN("관리자");

  private final String description;
}