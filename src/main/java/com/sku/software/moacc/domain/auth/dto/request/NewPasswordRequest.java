package com.sku.software.moacc.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "비밀번호 재설정 - 새 비밀번호 설정 DTO")
public class NewPasswordRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Schema(description = "사용자 아이디", example = "jaeyeon20")
    private String username;

    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "사용자 이메일", example = "jaeyeon20@gmail.com")
    private String email;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "비밀번호는 최소 8자 이상, 숫자 및 특수문자를 포함해야 합니다."
    )
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;

    @NotBlank(message = "인증 코드는 필수입니다.")
    @Schema(description = "이메일로 받은 인증 코드", example = "ABC123")
    private String code;
}
