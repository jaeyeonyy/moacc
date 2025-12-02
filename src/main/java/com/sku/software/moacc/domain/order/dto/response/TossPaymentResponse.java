package com.sku.software.moacc.domain.order.dto.response;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 토스페이먼츠 결제 승인 API 응답을 받기 위한 DTO
@Getter
@Setter
@ToString
public class TossPaymentResponse {

    // --- 필수 정보 ---

    private String version;
    private String paymentKey;

    // 토스가 반환하는 orderId는 항상 String입니다.
    private String orderId;

    private String orderName;

    // 최종 결제 금액 (상점 DB의 amount와 타입 통일을 위해 Integer 사용)
    private Integer totalAmount;

    private String status;
    private String method;
    private String approvedAt;


    // --- 추가적으로 필요한 정보 (선택적) ---

    private VirtualAccount virtualAccount;
    private Card card;
    private EasyPay easyPay;


    // --- 내부 클래스 정의 ---

    // 💳 카드 결제 정보
    @Getter
    @Setter
    public static class Card {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanDetails;
    }

    // 💰 가상 계좌 정보
    @Getter
    @Setter
    public static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
    }

    // 💳 간편 결제 정보
    @Getter
    @Setter
    public static class EasyPay {
        private String provider;
        private int amount;
    }
}