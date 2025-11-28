package com.sku.software.moacc.domain.product.exception;

import com.sku.software.moacc.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OptionValueErrorCode implements BaseErrorCode {
    OPTION_VALUE_ALREADY_EXISTS("OPT_VAL_400", "이미 존재하는 옵션 값입니다.", HttpStatus.BAD_REQUEST),
    OPTION_VALUE_NOT_FOUND("OPT_VAL_404", "옵션 값을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    OPTION_VALUE_CODE_ALREADY_EXISTS("OPT_VAL_400_CODE", "이미 존재하는 옵션 값 코드입니다.", HttpStatus.BAD_REQUEST),
    OPTION_VALUE_VALUE_ALREADY_EXISTS("OPT_VAL_400_VALUE", "이미 존재하는 옵션 값(value)입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
