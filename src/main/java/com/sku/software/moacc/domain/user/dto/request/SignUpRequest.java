package com.sku.software.moacc.domain.user.dto.request;

import com.sku.software.moacc.domain.user.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "SignUpRequest DTO", description = "사용자 회원가입을 위한 데이터 전송")
public class SignUpRequest {

  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @Schema(description = "사용자 아이디", example = "jaeyeon20@gmail.com")
  private String username;

  @NotBlank(message = "비밀번호 항목은 필수입니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$",
      message = "비밀번호는 최소 8자 이상, 숫자 및 특수문자를 포함해야 합니다.")
  @Schema(description = "비밀번호", example = "password123!")
  private String password;

  @NotBlank(message = "사용자 이름 항목은 필수입니다.")
  @Schema(description = "사용자 이름", example = "재연")
  private String name;

  @Schema(description = "사용자 언어", example = "KO")
  private Language language;

  @Schema(description = "사용자 소개", example = "안녕하세요, 재연입니다.")
  private String introduction;


}
