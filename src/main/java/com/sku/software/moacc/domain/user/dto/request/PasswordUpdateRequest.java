package com.sku.software.moacc.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PasswordUpdateRequest DTO", description = "비밀번호 변경을 위한 데이터 전송")
public class PasswordUpdateRequest {

  @NotBlank(message = "현재 비밀번호 항목은 필수입니다.")
  @Schema(description = "현재 비밀번호", example = "currentPassword123!")
  private String currentPassword;

  @NotBlank(message = "새 비밀번호 항목은 필수입니다.")
  @Schema(description = "새 비밀번호", example = "newPassword123!")
  private String newPassword;

}
