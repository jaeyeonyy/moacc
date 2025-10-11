package com.sku.software.moacc.global.security;

import com.sku.software.moacc.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String roleName = "ROLE_" + user.getAuthRole().name();
    return List.of(new SimpleGrantedAuthority(roleName));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  // 계정의 유효기간이 지났는지(만료됐는지)를 검사
  // 유료 구독 서비스 (MoAcc 패스 -> 배송비 무료)
  // 체험판 기간이 끝난 사용자
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 보안 정책상 로그인 실패 횟수 제한을 두는 서비스에서 사용
  // 로그인 실패 5회 이상 → 계정 잠금
  // 해킹/의심 행위 감지 시 자동 잠금
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 정기적인 비밀번호 변경 정책이 있는 기업/보안 시스템에서 사용
  // 비밀번호를 3개월 이상 변경하지 않았을 경우 로그인 차단
  // 보안 강화 시스템 (금융권, 관공서, 사내 시스템 등)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 거의 모든 서비스에서 사용됨 (가장 자주 쓰는 항목)
  // 보통 아래와 같은 경우에 false 처리
  // 이메일 인증 미완료
  // 회원 탈퇴 상태
  // 관리자에 의해 비활성화된 계정
  @Override
  public boolean isEnabled() {
    return true;
  }
}