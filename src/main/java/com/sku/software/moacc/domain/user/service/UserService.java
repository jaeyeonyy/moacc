package com.sku.software.moacc.domain.user.service;

import com.sku.software.moacc.domain.user.dto.request.LanguageUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.NameUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.PasswordUpdateRequest;
import com.sku.software.moacc.domain.user.dto.request.SignUpRequest;
import com.sku.software.moacc.domain.user.dto.response.UserResponse;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.enums.Language;
import com.sku.software.moacc.domain.user.enums.Role;
import com.sku.software.moacc.domain.user.exception.UserErrorCode;
import com.sku.software.moacc.domain.user.mapper.UserMapper;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import com.sku.software.moacc.global.exception.CustomException;
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
    log.info("[서비스] 회원가입 시도: username = {}", request.getUsername());
    if (userRepository.existsByUsername(request.getUsername())) {
      log.warn("[서비스] 이미 존재하는 사용자: username = {}", request.getUsername());
      throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
    }

    // 비밀번호 인코딩
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    // 유저 엔티티 생성
    User user = User.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .name(request.getName())
        .language(request.getLanguage())
        .introduction(request.getIntroduction())
        .authRole(Role.USER)
        .build();

    // 저장 및 로깅
    User savedUser = userRepository.save(user);
    log.info("[서비스] 회원가입 성공: username = {}", savedUser.getUsername());
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
   * 사용자의 언어 설정을 변경하는 서비스 메서드.
   * <p>
   * 주어진 사용자 ID에 해당하는 사용자를 데이터베이스에서 조회한 후, {@link LanguageUpdateRequest} 객체로 전달된 새로운 언어로 사용자 정보를
   * 갱신한다.
   * </p>
   *
   * @param userId      변경할 대상 사용자의 고유 ID
   * @param newLanguage 사용자의 새로운 언어 설정을 담고 있는 요청 DTO
   * @return 언어 변경 후의 {@link UserResponse} 객체
   * @throws CustomException {@link UserErrorCode#USER_NOT_FOUND} – 사용자를 찾을 수 없는 경우 발생
   */
  @Transactional
  public UserResponse changeLanguage(Long userId, LanguageUpdateRequest newLanguage) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    log.info("[서비스] 언어 변경 시도: username = {}", user.getUsername());

    // 언어 변경
    user.setLanguage(Language.valueOf(newLanguage.getNewLanguage()));
    log.info("[서비스] 언어 변경 성공: username = {}, newLanguage = {}", user.getUsername(), newLanguage);
    return userMapper.toUserResponse(user);
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
    user.setName(newName.getNewName());
    log.info("[서비스] 사용자 이름 변경 성공: username = {}, newName = {}", user.getUsername(), newName);
    return userMapper.toUserResponse(user);
  }
}
