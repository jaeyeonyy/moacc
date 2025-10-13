package com.sku.software.moacc.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "EmailVerificationRequest", description = "이메일 인증 요청 DTO")
public class EmailVerificationRequest {

  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수입니다.")
  @Schema(description = "인증할 이메일 주소", example = "user@example.com")
  private String email;

  @NotBlank(message = "인증 코드는 필수입니다.")
  @Schema(description = "인증 코드", example = "ABC123")
  private String code;

}
