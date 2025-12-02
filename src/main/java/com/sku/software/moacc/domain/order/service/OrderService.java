package com.sku.software.moacc.domain.order.service;

import com.sku.software.moacc.domain.order.dto.request.OrderCreateRequest;
import com.sku.software.moacc.domain.order.dto.response.OrderCreateResponse;
import com.sku.software.moacc.domain.order.dto.request.PaymentConfirmRequest;
import com.sku.software.moacc.domain.order.dto.response.PaymentConfirmResponse;
import com.sku.software.moacc.domain.order.dto.response.TossPaymentResponse;
import com.sku.software.moacc.domain.order.dto.response.OrderResponse; // OrderResponse import 추가
import com.sku.software.moacc.domain.order.entity.Order;
import com.sku.software.moacc.domain.order.entity.OrderItem;
import com.sku.software.moacc.domain.order.entity.OrderStatus;
import com.sku.software.moacc.domain.order.entity.Payment;
import com.sku.software.moacc.domain.order.entity.PaymentStatus;
import com.sku.software.moacc.domain.order.repository.OrderRepository;
import com.sku.software.moacc.domain.order.repository.PaymentRepository;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import com.sku.software.moacc.domain.cart.service.CartService; // CartService import 추가
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List; // List import 추가
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors; // Collectors import 추가

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CartService cartService; // CartService 주입
    private final RestTemplate restTemplate = new RestTemplate();

    // TossPaymentResponse DTO의 필드가 Integer로 수정되었다고 가정합니다.
    // DTO 파일은 아래에 다시 첨부합니다.

    @Value("${toss.secretKey}")
    private String secretKey;

    @Value("${toss.clientKey}")
    private String clientKey;

    private final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    @Transactional
    public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Order order = new Order();
        order.setUserId(userId); // JWT에서 추출한 userId 사용
        order.setTotalAmount(request.getTotalAmount());
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderStatus(OrderStatus.READY);

        String firstProductName = "";
        for (var itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setSkuId(itemReq.getSkuId());
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

        // 🚨 DUPLICATED_ORDER_ID 해결을 위해 고유한 String orderId 생성 🚨
        // [타임스탬프]-[DB_ID_패딩] 형태를 사용하여 6자 이상, 고유성 확보
        String timestamp = String.valueOf(System.currentTimeMillis());
        String dbIdPadded = String.format("%05d", savedOrder.getId());
        String tossOrderId = timestamp + "-" + dbIdPadded;

        // tossOrderId를 Order 엔티티에 저장
        savedOrder.setTossOrderId(tossOrderId);
        orderRepository.save(savedOrder);

        return OrderCreateResponse.builder()
                .orderId(savedOrder.getId()) // DB 내부 관리를 위한 Long ID
                .tossOrderId(tossOrderId)    // 토스에 전달할 고유 String ID
                .orderName(orderName)
                .customerName(user.getName()) // 고객명 추가
                .customerEmail(user.getEmail()) // 고객 이메일 추가
                .amount(savedOrder.getTotalAmount())
                .clientKey(clientKey)
                .build();
    }

    @Transactional
    public PaymentConfirmResponse confirmPayment(Long userId, PaymentConfirmRequest request) {
        // tossOrderId로 주문을 조회
        Order order = orderRepository.findByTossOrderId(request.getTossOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 ID를 찾을 수 없습니다: " + request.getTossOrderId()));

        // 주문이 현재 로그인한 사용자의 것인지 확인
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new IllegalArgumentException("해당 주문에 대한 권한이 없습니다.");
        }

        // 이미 결제가 완료된 주문인지 확인 (중복 요청 방지)
        if (order.getOrderStatus() == OrderStatus.PAID) {
            throw new IllegalArgumentException("이미 결제가 완료된 주문입니다.");
        }

        if (!Objects.equals(order.getTotalAmount(), request.getAmount())) {
            throw new IllegalArgumentException("요청 금액(" + request.getAmount() + ")이 주문 금액(" + order.getTotalAmount() + ")과 일치하지 않습니다.");
        }

        // 🚨 비관적 잠금을 사용하여 Payment 엔티티 조회 및 동시성 제어 🚨
        Optional<Payment> existingPayment = paymentRepository.findByIdWithLock(request.getPaymentKey());

        Payment payment;
        if (existingPayment.isPresent()) {
            payment = existingPayment.get();

            // 이미 완료된 경우 중복 처리 방지
            if (payment.getPaymentStatus() == PaymentStatus.DONE) {
                throw new RuntimeException("결제 처리가 이미 완료되었습니다. 중복 처리를 방지합니다.");
            }
        } else {
            // 🚨 새로운 Payment 생성 시 즉시 Order 관계 설정 🚨
            payment = new Payment(request.getPaymentKey());
            payment.setOrder(order); // Order 관계 즉시 설정
            payment.setPaymentStatus(PaymentStatus.READY); // 초기 상태 설정
        }

        // 토스페이먼츠 API 인증을 위한 Authorization 헤더 생성
        String authString = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        // HTTP 헤더 설정
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Basic " + authString);
        headers.set("Content-Type", "application/json");

        var tossRequest = new java.util.HashMap<String, Object>();
        tossRequest.put("paymentKey", request.getPaymentKey());
        // 클라이언트에서 받은 tossOrderId를 그대로 토스 API에 전달
        tossRequest.put("orderId", request.getTossOrderId());
        tossRequest.put("amount", request.getAmount());

        // HttpEntity로 헤더와 바디를 함께 전송
        org.springframework.http.HttpEntity<java.util.HashMap<String, Object>> httpEntity =
            new org.springframework.http.HttpEntity<>(tossRequest, headers);

        try {
            ResponseEntity<TossPaymentResponse> responseEntity = restTemplate.postForEntity(
                    TOSS_CONFIRM_URL,
                    httpEntity,
                    TossPaymentResponse.class
            );

            if (responseEntity.getBody() == null) {
                throw new RuntimeException("토스 결제 승인 실패: 응답이 없습니다.");
            }

            TossPaymentResponse tossResponse = responseEntity.getBody();

            if ("DONE".equals(tossResponse.getStatus())) {
                // Order 상태 업데이트
                order.setOrderStatus(OrderStatus.PAID);
                orderRepository.save(order);

                // Payment 정보 설정 - READY에서 DONE으로 변경
                payment.setPaymentStatus(PaymentStatus.DONE);
                payment.setMethod(tossResponse.getMethod());
                payment.setAmount(tossResponse.getTotalAmount());
                payment.setApprovedAt(LocalDateTime.now());


                paymentRepository.save(payment);

                // 주문 완료 후 장바구니 비우기
                cartService.clearCart(userId);

            } else {
                throw new IllegalStateException("결제 상태가 완료(DONE)가 아닙니다: " + tossResponse.getStatus());
            }

            return PaymentConfirmResponse.builder()
                    .orderId(order.getId())
                    .paymentKey(tossResponse.getPaymentKey())
                    .amount(tossResponse.getTotalAmount())
                    .status(tossResponse.getStatus())
                    .message("결제가 성공적으로 완료되었습니다.")
                    .build();

        } catch (org.springframework.web.client.HttpServerErrorException e) {
            // 토스페이먼츠 API 서버 오류 처리
            String errorMessage = "토스페이먼츠 API 오류: " + e.getResponseBodyAsString();

            // 기존 요청 처리중 오류인 경우
            if (e.getResponseBodyAsString().contains("S008") || e.getResponseBodyAsString().contains("기존 요청을 처리중")) {
                throw new RuntimeException("결제 처리가 이미 진행 중입니다. 잠시 후 다시 시도해주세요.");
            }

            throw new RuntimeException(errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        log.info("DB에서 구매 여부 확인. 사용자 ID: {}, 상품 ID: {}, 주문 상태: {}", userId, productId, OrderStatus.PAID);
        boolean result = orderRepository.existsByUserIdAndOrderItems_ProductIdAndOrderStatus(userId, productId, OrderStatus.PAID);
        log.info("DB 조회 결과: {}", result);
        return result;
    }

    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findFirst200ByUserIdOrderByIdDesc(userId);
        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
