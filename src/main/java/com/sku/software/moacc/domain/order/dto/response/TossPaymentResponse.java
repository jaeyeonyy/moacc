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

    // 토스 API 응답 버전
    private String version;

    // 결제 건의 고유 ID (PaymentKey)
    private String paymentKey;

    // 상점에서 요청했던 주문 ID
    private String orderId;

    // 토스에 보냈던 주문명
    private String orderName;

    // 최종 결제 금액
    private Long totalAmount;

    // 결제 상태 (READY, DONE, CANCELED 등)
    private String status;

    // 결제 수단 (카드, 가상계좌, 계좌이체 등)
    private String method;

    // 결제 승인 날짜 및 시간 (ISO 8601 형식)
    private String approvedAt;


    // --- 추가적으로 필요한 정보 (선택적) ---

    // 가상계좌 정보 (가상계좌 결제 시에만 존재)
    private VirtualAccount virtualAccount;

    // 카드 정보 (카드 결제 시에만 존재)
    private Card card;

    // 간편 결제 정보 (간편 결제 시에만 존재)
    private EasyPay easyPay;


    // --- 내부 클래스 정의 ---

    // 💳 카드 결제 정보
    @Getter
    @Setter
    public static class Card {
        private String issuerCode; // 카드사 코드
        private String acquirerCode; // 매입사 코드
        private String number; // 마스킹된 카드 번호
        private Integer installmentPlanDetails; // 할부 개월 수 (00:일시불)
        // ... 필요한 다른 필드 추가 가능
    }

    // 💰 가상 계좌 정보
    @Getter
    @Setter
    public static class VirtualAccount {
        private String accountType; // 계좌 타입
        private String accountNumber; // 계좌 번호
        private String bankCode; // 은행 코드
        private String customerName; // 입금해야 하는 사람 이름
        // ... 필요한 다른 필드 추가 가능
    }

    // 💳 간편 결제 정보
    @Getter
    @Setter
    public static class EasyPay {
        private String provider; // 간편 결제사 (토스페이, 네이버페이, 카카오페이 등)
        private int amount; // 간편 결제로 결제된 금액
        // ... 필요한 다른 필드 추가 가능
    }
}