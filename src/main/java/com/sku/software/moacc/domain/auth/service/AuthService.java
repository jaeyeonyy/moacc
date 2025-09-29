package com.sku.software.moacc.domain.auth.service;

import com.sku.software.moacc.domain.auth.dto.request.LoginRequest;
import com.sku.software.moacc.domain.auth.dto.response.LoginResponse;
import com.sku.software.moacc.domain.auth.mapper.AuthMapper;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.exception.UserErrorCode;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import com.sku.software.moacc.global.exception.CustomException;
import com.sku.software.moacc.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;
  private final AuthMapper authMapper;

  /**
   * 사용자 로그인 서비스 메서드.
   * <p>
   * 주어진 {@link LoginRequest} 객체로부터 사용자 정보를 추출하여 인증을 시도하고, 성공 시 액세스 토큰과 리프레시 토큰을 발급한다.
   * </p>
   *
   * @param loginRequest 로그인 요청 DTO
   * @return 로그인 응답 DTO
   * @throws CustomException {@link UserErrorCode#USER_NOT_FOUND} – 존재하지 않는 사용자 이름인 경우 발생
   */
  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());

    // 인증 처리
    authenticationManager.authenticate(authenticationToken);

    // 액세스 토큰 및 리프레시 토큰 발급
    String accessToken = jwtProvider.createAccessToken(user.getUsername());
    String refreshToken = jwtProvider.createRefreshToken(user.getUsername(),
        UUID.randomUUID().toString());

    // 리프레시 토큰 저장
    user.createRefreshToken(refreshToken);

    // Access Token의 만료 시간을 가져옴
    Long expirationTime = jwtProvider.getExpiration(accessToken);

    // 로그인 성공 로깅
    log.info("로그인 성공: {}", user.getUsername());

    // 로그인 응답 변환
    return authMapper.toLoginResponse(user, accessToken, expirationTime);

  }
}
