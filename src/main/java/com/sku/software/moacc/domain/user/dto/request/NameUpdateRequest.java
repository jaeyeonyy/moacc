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
public class NameUpdateRequest {

  @NotBlank(message = "새로운 사용자 이름 항목은 필수입니다.")
  @Schema(description = "새로운 사용자 이름", example = "재연1234")
  private String newName;

}
