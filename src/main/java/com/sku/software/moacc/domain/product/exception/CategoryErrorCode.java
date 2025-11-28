package com.sku.software.moacc.domain.product.exception;

import com.sku.software.moacc.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode implements BaseErrorCode {
    CATEGORY_NOT_FOUND("CATEGORY_404", "카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS("CATEGORY_400", "이미 존재하는 카테고리입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}

