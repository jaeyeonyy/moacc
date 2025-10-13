package com.sku.software.moacc.domain.user.service;

import com.sku.software.moacc.domain.user.dto.request.NameUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.PasswordUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.SignUpRequest;
import com.sku.software.moacc.domain.user.dto.response.UserResponse;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.enums.Role;
import com.sku.software.moacc.domain.user.exception.UserErrorCode;
import com.sku.software.moacc.domain.user.mapper.UserMapper;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import com.sku.software.moacc.global.exception.CustomException;
import com.sku.software.moacc.global.infra.redis.auth.RedisAuthCodeStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final RedisAuthCodeStore redisAuthCodeStore;

  /**
   * 사용자가 회원가입을 시도하는 서비스 메서드.
   * <p>
   * 주어진 {@link SignUpRequest} 객체로부터 사용자 정보를 추출하여 새로운 {@link User} 엔티티를 생성하고, 이를 데이터베이스에 저장한다.
   * </p>
   *
   * @param request 회원가입 요청 DTO
   * @return 저장된 사용자 정보가 담긴 {@link UserResponse} 객체
   * @throws CustomException {@link UserErrorCode#USERNAME_ALREADY_EXISTS} – 이미 존재하는 사용자 이름인 경우 발생
   */
  @Transactional
  public UserResponse signUp(SignUpRequest request) {
    String username = request.getUsername();
    String email = request.getEmail();
    log.info("[서비스] 회원가입 시도: username = {}, email = {}", username, email);

    // 1. 이메일 인증 여부 확인
    if (!redisAuthCodeStore.isEmailVerified(email)) {
      log.warn("[서비스] 이메일 미인증: email = {}", email);
      throw new CustomException(UserErrorCode.EMAIL_NOT_VERIFIED);
    }

    // 2. 중복 사용자 확인 (username)
    if (userRepository.existsByUsername(username)) {
      log.warn("[서비스] 이미 존재하는 사용자: username = {}", username);
      throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
    }

    // 3. 중복 이메일 확인
    if (userRepository.existsByEmail(email)) {
      log.warn("[서비스] 이미 존재하는 이메일: email = {}", email);
      throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
    }

    // 4. 비밀번호 인코딩
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    // 5. 유저 엔티티 생성
    User user = User.builder()
        .username(username)
        .email(email)
        .password(encodedPassword)
        .nickname(request.getName())
        .authRole(Role.USER)
        .build();

    // 6. 저장
    User savedUser = userRepository.save(user);
    log.info("[서비스] 회원가입 성공: username = {}, email = {}", username, email);

    // 7. Redis에서 인증 완료 플래그 삭제
    redisAuthCodeStore.deleteVerifiedFlag(email);

    return userMapper.toUserResponse(savedUser);
  }

  /**
   * 사용자의 비밀번호를 변경하는 서비스 메서드.
   * <p>
   * 주어진 사용자 ID에 해당하는 사용자를 데이터베이스에서 조회한 후, 현재 비밀번호와 새 비밀번호를 비교하여 비밀번호를 변경한다.
   * </p>
   *
   * @param userId                변경할 대상 사용자의 고유 ID
   * @param passwordUpdateRequest 비밀번호 변경 요청 DTO
   * @throws CustomException {@link UserErrorCode#USER_NOT_FOUND} – 사용자를 찾을 수 없는 경우 발생
   *                         {@link UserErrorCode#PASSWORD_MISMATCH} – 현재 비밀번호가 일치하지 않는 경우 발생
   */
  @Transactional
  public void changePassword(Long userId, PasswordUpdateRequest passwordUpdateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    log.info("[서비스] 비밀번호 변경 시도: username = {}", user.getUsername());

    // 현재 비밀번호와 입력된 비밀번호 비교
    if (!passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), user.getPassword())) {
      log.warn("[서비스] 비밀번호가 일치하지 않습니다.: username = {}", user.getUsername());
      throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
    }

    // 새 비밀번호 인코딩
    String encodedPassword = passwordEncoder.encode(passwordUpdateRequest.getNewPassword());

    // 비밀번호 변경
    user.setPassword(encodedPassword);
    log.info("[서비스] 비밀번호 변경 성공: username = {}", user.getUsername());

  }


  /**
   * 사용자의 이름을 변경하는 서비스 메서드.
   * <p>
   * 주어진 사용자 ID에 해당하는 사용자를 데이터베이스에서 조회한 후, {@link NameUpdateRequest} 객체로 전달된 새로운 이름으로 사용자 정보를 갱신한다.
   *
   * @param userId  변경할 대상 사용자의 고유 ID
   * @param newName 사용자의 새로운 이름을 담고 있는 요청 DTO
   * @return 이름 변경 후의 {@link UserResponse} 객체
   * @throws CustomException {@link UserErrorCode#USER_NOT_FOUND} – 사용자를 찾을 수 없는 경우 발생
   */
  @Transactional
  public UserResponse changeName(Long userId, NameUpdateRequest newName) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    log.info("[서비스] 사용자 이름 변경 시도: username = {}", user.getUsername());

    // 이름 변경
    user.setNickname(newName.getNewName());
    log.info("[서비스] 사용자 이름 변경 성공: username = {}, newName = {}", user.getUsername(), newName);
    return userMapper.toUserResponse(user);
  }
}
