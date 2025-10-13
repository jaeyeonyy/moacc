package com.sku.software.moacc.domain.auth.service;

import com.sku.software.moacc.global.infra.redis.auth.RedisAuthCodeStore;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisAuthCodeStore redisAuthCodeStore;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 랜덤번호 6자리
    public String createdCertifyNum() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public MimeMessage createMail(String email, String authCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("이메일 인증");
        String body = "";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + authCode + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    // 메일 보내기 (성공 시 true, 실패 시 false)
    public boolean sendEmail(String toEmail) {
        try {
            String authCode = createdCertifyNum();
            MimeMessage emailForm = createMail(toEmail, authCode);

            // 인증 코드 저장 (5분)
            redisAuthCodeStore.createRedisData(toEmail, authCode);

            javaMailSender.send(emailForm);
            return true;
        } catch (MessagingException | MailException e) {
            // 메일 생성/전송 실패 시 Redis에 저장된 코드가 있다면 삭제 (안정성)
            try {
                if (redisAuthCodeStore.existData(toEmail)) {
                    redisAuthCodeStore.deleteData(toEmail);
                }
            } catch (Exception ex) {
                // 무시
            }
            return false;
        }
    }

    // 코드 검증 및 인증 완료 처리
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisAuthCodeStore.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }

        boolean isValid = codeFoundByEmail.equals(code);
        if (isValid) {
            // 인증 성공 시:
            // 1. 인증 코드 삭제
            redisAuthCodeStore.deleteData(email);
            // 2. 인증 완료 플래그 설정 (30분)
            redisAuthCodeStore.setVerifiedFlag(email);
        }
        return isValid;
    }



}