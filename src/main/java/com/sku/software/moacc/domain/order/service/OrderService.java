package com.sku.software.moacc.domain.order.service;

import com.sku.software.moacc.domain.order.dto.request.OrderCreateRequest;
import com.sku.software.moacc.domain.order.dto.response.OrderCreateResponse;
import com.sku.software.moacc.domain.order.dto.request.PaymentConfirmRequest;
import com.sku.software.moacc.domain.order.dto.response.PaymentConfirmResponse;
import com.sku.software.moacc.domain.order.dto.response.TossPaymentResponse;
import com.sku.software.moacc.domain.order.entity.Order;
import com.sku.software.moacc.domain.order.entity.OrderItem;
import com.sku.software.moacc.domain.order.entity.OrderStatus;
import com.sku.software.moacc.domain.order.entity.Payment;
import com.sku.software.moacc.domain.order.entity.PaymentStatus;
import com.sku.software.moacc.domain.order.repository.OrderRepository;
import com.sku.software.moacc.domain.order.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secretKey}")
    private String secretKey;

    @Value("${toss.clientKey}")
    private String clientKey;

    private final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    @Transactional
    public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
        Order order = new Order();
        order.setUserId(userId); // JWT에서 추출한 userId 사용
        order.setTotalAmount(request.getTotalAmount());
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderStatus(OrderStatus.READY);

        String firstProductName = "";
        for (var itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setProductName(itemReq.getProductName());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(itemReq.getPrice());
            item.setOrder(order);

            if (firstProductName.isEmpty()) {
                firstProductName = itemReq.getProductName();
            }
        }

        String orderName = firstProductName;
        if (request.getItems().size() > 1) {
            orderName = orderName + " 외 " + (request.getItems().size() - 1) + "건";
        }
        order.setOrderName(orderName);

        Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.builder()
                .orderId(savedOrder.getId())
                .orderName(orderName)
                .amount(savedOrder.getTotalAmount())
                .clientKey(clientKey)
                .build();
    }

    @Transactional
    public PaymentConfirmResponse confirmPayment(Long userId, PaymentConfirmRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID를 찾을 수 없습니다."));

        // 주문이 현재 로그인한 사용자의 것인지 확인
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new IllegalArgumentException("해당 주문에 대한 권한이 없습니다.");
        }

        if (!Objects.equals(order.getTotalAmount(), request.getAmount())) {
            throw new IllegalArgumentException("요청 금액(" + request.getAmount() + ")이 주문 금액(" + order.getTotalAmount() + ")과 일치하지 않습니다.");
        }

        String authString = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        var tossRequest = new java.util.HashMap<String, Object>();
        tossRequest.put("paymentKey", request.getPaymentKey());
        tossRequest.put("orderId", String.valueOf(request.getOrderId()));
        tossRequest.put("amount", request.getAmount());

        // Note: TossPaymentResponse DTO는 별도로 정의해야 합니다. (여기서는 예시를 위해 임시로 사용)
        ResponseEntity<TossPaymentResponse> responseEntity = restTemplate.postForEntity(
                TOSS_CONFIRM_URL,
                tossRequest,
                TossPaymentResponse.class
        );

        if (responseEntity.getStatusCode().isError() || responseEntity.getBody() == null) {
            // 토스 API 응답에 따른 구체적인 에러 처리 로직 추가 필요
            throw new RuntimeException("토스 결제 승인 실패: " + responseEntity.getStatusCode());
        }

        TossPaymentResponse tossResponse = responseEntity.getBody();

        if ("DONE".equals(tossResponse.getStatus())) {
            order.setOrderStatus(OrderStatus.PAID);
            orderRepository.save(order);

            Payment payment = new Payment();
            payment.setPaymentKey(tossResponse.getPaymentKey());
            payment.setPaymentStatus(PaymentStatus.DONE);
            payment.setMethod(tossResponse.getMethod());
            // 🚨 수정 필요 부분 🚨
            // Long -> int로 변환하여 Payment 엔티티에 저장
            payment.setAmount(tossResponse.getTotalAmount().intValue());

            payment.setApprovedAt(LocalDateTime.now());
            payment.setOrder(order);
            paymentRepository.save(payment);
        } else {
            throw new IllegalStateException("결제 상태가 완료(DONE)가 아닙니다: " + tossResponse.getStatus());
        }

        return PaymentConfirmResponse.builder()
                .orderId(order.getId())
                .paymentKey(tossResponse.getPaymentKey())
                // 🚨 수정 필요 부분 🚨
                // Long -> int로 변환하여 Response DTO에 포함
                .amount(tossResponse.getTotalAmount().intValue())
                .status(tossResponse.getStatus())
                .message("결제가 성공적으로 완료되었습니다.")
                .build();
    }

}