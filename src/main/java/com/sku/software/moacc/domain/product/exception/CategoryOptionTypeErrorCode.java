package com.sku.software.moacc.domain.product.exception;

import com.sku.software.moacc.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CategoryOptionTypeErrorCode implements BaseErrorCode {
    OPTION_TYPE_ALREADY_EXISTS("CAT_OPT_400", "이미 존재하는 카테고리 옵션 타입(code)입니다.", HttpStatus.BAD_REQUEST),
    OPTION_TYPE_NOT_FOUND("CAT_OPT_404", "카테고리 옵션 타입을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}

