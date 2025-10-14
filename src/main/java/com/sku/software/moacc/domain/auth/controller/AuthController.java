package com.sku.software.moacc.domain.auth.controller;

import com.sku.software.moacc.domain.auth.dto.request.EmailSendRequest;
import com.sku.software.moacc.domain.auth.dto.request.EmailVerificationRequest;
import com.sku.software.moacc.domain.auth.dto.request.LoginRequest;
import com.sku.software.moacc.domain.auth.dto.request.NewPasswordRequest;
import com.sku.software.moacc.domain.auth.dto.request.PasswordResetRequest;
import com.sku.software.moacc.domain.auth.dto.response.LoginResponse;
import com.sku.software.moacc.domain.auth.service.AuthService;
import com.sku.software.moacc.domain.auth.service.MailService;
import com.sku.software.moacc.domain.user.exception.UserErrorCode;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import com.sku.software.moacc.global.exception.CustomException;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auths")
@Tag(name = "Auth", description = "Auth 관리 API")
public class AuthController {

  private final AuthService authService;
  private final MailService mailService;
  private final UserRepository userRepository;

  @Operation(summary = "사용자 로그인", description = "사용자 로그인을 위한 API")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
    LoginResponse loginResponse = authService.login(loginRequest);

    // refreshToken 가져오기
    String refreshToken = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND))
        .getRefreshToken();

    // Set-Cookie 설정 (HttpOnly + Secure)
    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    // refreshTokenCookie.setSecure(true);  // HTTPS일 때만
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);  // 예: 7일

    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(BaseResponse.success("로그인에 성공했습니다.", loginResponse));
  }

  @Operation(summary = "이메일 인증 코드 전송", description = "사용자 이메일로 인증 코드를 전송합니다.")
  @PostMapping("/email/verification")
  public ResponseEntity<BaseResponse<String>> requestAuthcode(@RequestBody @Valid EmailSendRequest request) {
    boolean isSend = mailService.sendEmail(request.getEmail());
    if (!isSend) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(BaseResponse.error(400, "인증 코드 발급에 실패하였습니다."));
    }
    return ResponseEntity.ok(BaseResponse.success("인증 코드가 전송되었습니다.", "OK"));
  }

  @Operation(summary = "이메일 인증 코드 검증", description = "이메일로 받은 인증코드를 검증합니다.")
  @PostMapping("/email/verification/confirm")
  public ResponseEntity<BaseResponse<String>> verifyAuthCode(@RequestBody @Valid EmailVerificationRequest request) {
    boolean isVerified = mailService.verifyEmailCode(request.getEmail(), request.getCode());

    if (!isVerified) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(BaseResponse.error(400, "인증 코드가 유효하지 않거나 만료되었습니다."));
    }

    return ResponseEntity.ok(BaseResponse.success("인증이 완료되었습니다.", "OK"));
  }

  @Operation(summary = "비밀번호 재설정 - 인증코드 요청", description = "아이디 및 이메일로 비밀번호 재설정 인증코드를 전송합니다.")
  @PostMapping("/password/reset/request")
  public ResponseEntity<BaseResponse<String>> requestPasswordReset(@RequestBody @Valid PasswordResetRequest request) {
    boolean sent = authService.requestPasswordReset(request);
    if (!sent) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(BaseResponse.error(400, "비밀번호 재설정 메일 발송에 실패했습니다."));
    }
    return ResponseEntity.ok(BaseResponse.success("비밀번호 재설정 인증코드가 전송되었습니다.", "OK"));
  }

  @Operation(summary = "비밀번호 재설정 - 코드 확인 및 비밀번호 변경", description = "인증코드 확인 후 새 비밀번호로 변경합니다.")
  @PostMapping("/password/reset/confirm")
  public ResponseEntity<BaseResponse<String>> confirmPasswordReset(@RequestBody @Valid NewPasswordRequest request) {
    try {
      authService.resetPassword(request);
      return ResponseEntity.ok(BaseResponse.success("비밀번호가 성공적으로 변경되었습니다.", "OK"));
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(BaseResponse.error(400, e.getMessage()));
    }
  }
}
