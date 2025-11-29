package com.sku.software.moacc.domain.order.controller;



import com.sku.software.moacc.domain.order.dto.request.OrderCreateRequest;
import com.sku.software.moacc.domain.order.dto.request.PaymentConfirmRequest;
import com.sku.software.moacc.domain.order.dto.response.OrderCreateResponse;
import com.sku.software.moacc.domain.order.dto.response.PaymentConfirmResponse;
import com.sku.software.moacc.domain.order.service.OrderService;
import com.sku.software.moacc.global.response.BaseResponse;
import com.sku.software.moacc.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * [1] 주문 생성 API
     */
    @PostMapping
    public BaseResponse<OrderCreateResponse> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OrderCreateRequest request) {

        Long userId = userDetails.getUser().getId();
        OrderCreateResponse response = orderService.createOrder(userId, request);

        return BaseResponse.success("주문이 생성되었습니다. 결제 위젯을 호출하세요.", response);
    }

    /**
     * [2] 결제 승인 API (토스페이먼츠 Confirm)
     */
    @PostMapping("/confirm")
    public BaseResponse<PaymentConfirmResponse> confirmPayment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PaymentConfirmRequest request) {

        Long userId = userDetails.getUser().getId();
        PaymentConfirmResponse response = orderService.confirmPayment(userId, request);

        return BaseResponse.success("결제가 성공적으로 완료되었습니다.", response);
    }
}