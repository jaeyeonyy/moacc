package com.sku.software.moacc.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum CategoryType {
  PHONE_CASE("PHONE_CASE", "휴대폰케이스"),
  SCREEN_PROTECTOR("SCREEN_PROTECTOR", "보호필름"),
  SELFIE_STAND("SELFIE_STAND", "셀카봉/거치대"),
  POWER_BANK("POWER_BANK", "보조배터리"),
  CABLE_CHARGER("CABLE_CHARGER", "케이블/충전기"),
  MEMORY_CARD("MEMORY_CARD", "메모리 카드"),
  WATERPROOF_CASE("WATERPROOF_CASE", "방수팩/방수케이스");

  private final String code;
  private final String displayName;

}
