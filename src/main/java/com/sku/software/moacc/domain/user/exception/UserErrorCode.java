package com.sku.software.moacc.domain.user.exception;

import com.sku.software.moacc.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND("USER001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXISTS("USER002", "이미 존재하는 아이디입니다.", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("USER003", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED("USER004", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("USER005", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    EMAIL_MISMATCH("USER006", "아이디와 이메일이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RESET_CODE("USER007", "비밀번호 재설정 코드가 유효하지 않거나 만료되었습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
