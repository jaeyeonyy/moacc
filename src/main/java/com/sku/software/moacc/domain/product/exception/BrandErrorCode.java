package com.sku.software.moacc.domain.product.exception;

import com.sku.software.moacc.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrandErrorCode implements BaseErrorCode {
    BRAND_NOT_FOUND("BRAND_404", "존재하지 않는 브랜드입니다.", 404),
    BRAND_ALREADY_EXISTS("BRAND_400", "이미 존재하는 브랜드입니다.", 400);

    private final String code;
    private final String message;
    private final int status;
}
