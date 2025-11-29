package com.sku.software.moacc.domain.cart.controller;

import com.sku.software.moacc.domain.cart.dto.request.CartRequest;
import com.sku.software.moacc.domain.cart.dto.request.QuantityUpdateRequest;
import com.sku.software.moacc.domain.cart.dto.response.CartItemResponse;
import com.sku.software.moacc.domain.cart.service.CartService;
import com.sku.software.moacc.global.response.BaseResponse;
import com.sku.software.moacc.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  /**
   * 1. GET: 현재 사용자의 장바구니 전체 조회
   * GET /api/carts
   */
  @GetMapping
  public ResponseEntity<BaseResponse<List<CartItemResponse>>> getCart(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getUser().getId();
    List<CartItemResponse> items = cartService.getCartItems(userId);
    return ResponseEntity.ok(BaseResponse.success(items));
  }

  /**
   * 2. POST: 장바구니에 상품 추가/수량 변경
   * POST /api/carts/items
   * Body: { "productSkuId": 101, "quantity": 2 }
   */
  @PostMapping("/items")
  public ResponseEntity<BaseResponse<CartItemResponse>> addItemToCart(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody CartRequest request) {

    Long userId = userDetails.getUser().getId();
    CartItemResponse addedItem = cartService.addOrUpdateItem(
        userId, request.getProductSkuId(), request.getQuantity());

    return ResponseEntity.ok(BaseResponse.success("장바구니에 상품이 추가되었습니다.", addedItem));
  }

  /**
   * 3. PATCH: 장바구니 항목의 수량 수정
   * PATCH /api/carts/items/{itemId}
   * Body: { "quantity": 5 }
   */
  @PatchMapping("/items/{cartItemId}")
  public ResponseEntity<BaseResponse<CartItemResponse>> updateItemQuantity(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long cartItemId,
      @RequestBody QuantityUpdateRequest request) {

    Long userId = userDetails.getUser().getId();
    CartItemResponse updatedItem = cartService.updateItemQuantity(
        userId, cartItemId, request.getQuantity());

    return ResponseEntity.ok(BaseResponse.success("수량이 변경되었습니다.", updatedItem));
  }

  /**
   * 4. DELETE: 특정 장바구니 항목 제거
   * DELETE /api/carts/items/{itemId}
   */
  @DeleteMapping("/items/{cartItemId}")
  public ResponseEntity<BaseResponse<Void>> removeItemFromCart(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long cartItemId) {

    Long userId = userDetails.getUser().getId();
    cartService.removeItem(userId, cartItemId);
    return ResponseEntity.ok(BaseResponse.success("장바구니 항목이 삭제되었습니다.", null));
  }

  /**
   * 5. DELETE: 장바구니 전체 비우기
   * DELETE /api/carts
   */
  @DeleteMapping
  public ResponseEntity<BaseResponse<Void>> clearCart(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getUser().getId();
    cartService.clearCart(userId);
    return ResponseEntity.ok(BaseResponse.success("장바구니가 비워졌습니다.", null));
  }
}