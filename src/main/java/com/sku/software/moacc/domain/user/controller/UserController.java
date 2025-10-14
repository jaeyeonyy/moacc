package com.sku.software.moacc.domain.user.controller;

import com.sku.software.moacc.domain.user.dto.request.NameUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.PasswordUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.SignUpRequest;
import com.sku.software.moacc.domain.user.dto.response.UserResponse;
import com.sku.software.moacc.domain.user.service.UserService;
import com.sku.software.moacc.global.response.BaseResponse;
import com.sku.software.moacc.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User 관리 API")
@Slf4j
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원가입 API", description = "사용자 회원가입을 위한 API")
  @PostMapping("/sign-up")
  public ResponseEntity<BaseResponse<UserResponse>> signUp(
      @RequestBody @Valid SignUpRequest signUpRequest) {
    UserResponse response = userService.signUp(signUpRequest);
    return ResponseEntity.ok(BaseResponse.success("회원가입이 완료되었습니다.", response));
  }


  @Operation(summary = "비밀번호 변경 API", description = "사용자 비밀번호 변경을 위한 API")
  @PatchMapping("/password")
  public ResponseEntity<BaseResponse<Void>> changePassword(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody @Valid PasswordUpdateRequest passwordUpdateRequest) {
    userService.changePassword(userDetails.getUser().getId(), passwordUpdateRequest);
    return ResponseEntity.ok(BaseResponse.success("비밀번호가 변경되었습니다.", null));
  }


  @Operation(summary = "사용자 이름 변경 API", description = "사용자 이름 변경을 위한 API")
  @PatchMapping("/name")
  public ResponseEntity<BaseResponse<UserResponse>> changeName(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody @Valid NameUpdateRequest nameUpdateRequest) {
    UserResponse response = userService.changeName(userDetails.getUser().getId(),
        nameUpdateRequest);
    return ResponseEntity.ok(BaseResponse.success("사용자 이름이 변경되었습니다.", response));
  }

  @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 프로필을 반환합니다.")
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserResponse>> getMyProfile(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserResponse response = userService.getMyProfile(userDetails.getUser().getId());
    return ResponseEntity.ok(BaseResponse.success("내 정보 조회에 성공했습니다.", response));
  }

}
