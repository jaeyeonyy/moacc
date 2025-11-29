package com.sku.software.moacc.domain.cart.service;

import com.sku.software.moacc.domain.cart.dto.response.CartItemResponse;
import com.sku.software.moacc.domain.cart.entity.Cart;
import com.sku.software.moacc.domain.cart.entity.CartItem;
import com.sku.software.moacc.domain.cart.repository.CartItemRepository;
import com.sku.software.moacc.domain.cart.repository.CartRepository;
import com.sku.software.moacc.domain.product.entity.ProductSku;
import com.sku.software.moacc.domain.product.repository.ProductSkuRepository;
import com.sku.software.moacc.domain.user.entity.User;
import com.sku.software.moacc.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  // User와 ProductSku를 조회하기 위한 Repository 또는 Service가 필요합니다.
  private final UserRepository userRepository;
  private final ProductSkuRepository productSkuRepository;

  /**
   * 상품을 장바구니에 추가하거나 기존 항목의 수량을 증가시킵니다.
   * @param userId 장바구니를 소유한 사용자 ID
   * @param productSkuId 담을 상품 SKU ID
   * @param quantity 담을 수량
   * @return 추가되거나 수정된 CartItem DTO
   */
  public CartItemResponse addOrUpdateItem(Long userId, Long productSkuId, int quantity) {
    // 1. 사용자(Cart) 찾기: 없으면 생성
    Cart cart = getOrCreateCart(userId);

    // 2. 상품 SKU 찾기 (유효성 검사)
    ProductSku productSku = productSkuRepository.findById(productSkuId)
        .orElseThrow(() -> new EntityNotFoundException("상품 옵션을 찾을 수 없습니다."));

    // 3. 기존 항목이 있는지 확인 (cart_id + product_sku_id 복합 유니크 제약 활용)
    Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductSkuId(
        cart.getId(), productSkuId);

    CartItem cartItem;
    if (existingItem.isPresent()) {
      // 항목이 이미 존재하면: 수량 업데이트 (Update)
      cartItem = existingItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + quantity);

      // TODO: 최대/최소 수량 및 재고 초과 여부 검사 로직 추가
    } else {
      // 항목이 없으면: 새로 생성 (Create)
      cartItem = CartItem.builder()
          .cart(cart)
          .productSku(productSku)
          .quantity(quantity)
          .build();
    }

    CartItem savedItem = cartItemRepository.save(cartItem);
    return CartItemResponse.fromEntity(savedItem);
  }

  // 장바구니를 가져오거나 없으면 새로 생성하는 메서드
  private Cart getOrCreateCart(Long userId) {
    return cartRepository.findByUserId(userId)
        .orElseGet(() -> {
          // 실제 User 엔티티를 찾아 설정해야 합니다.
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

          Cart newCart = Cart.newOf(user);
          return cartRepository.save(newCart);
        });
  }

  /**
   * 특정 사용자의 장바구니 항목 전체를 조회합니다.
   * @param userId 사용자 ID
   * @return 장바구니 항목 리스트
   */
  @Transactional(readOnly = true)
  public List<CartItemResponse> getCartItems(Long userId) {
    Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

    if (cartOptional.isEmpty()) {
      // 장바구니가 없으면 빈 리스트 반환 (새 장바구니 생성은 readOnly 트랜잭션에서 불가능하므로)
      return List.of();
    }

    Cart cart = cartOptional.get();

    // Cart 엔티티의 List<CartItem>을 사용하거나, Repository를 이용해 직접 조회합니다.
    List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

    // DTO로 변환하여 반환
    return items.stream()
        .map(CartItemResponse::fromEntity)
        .collect(Collectors.toList());
  }



  /**
   * 장바구니 항목의 수량을 수정합니다.
   * @param userId 현재 로그인한 사용자 ID
   * @param cartItemId 수정할 장바구니 항목 ID
   * @param newQuantity 새로운 수량
   * @return 수정된 CartItem DTO
   */
  public CartItemResponse updateItemQuantity(Long userId, Long cartItemId, int newQuantity) {
    if (newQuantity <= 0) {
      // 수량이 0이거나 음수이면 삭제 로직을 호출하거나 예외 처리
        throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
    }

    CartItem item = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new EntityNotFoundException("장바구니 항목을 찾을 수 없습니다."));

    // 해당 항목이 현재 로그인한 사용자의 장바구니에 속하는지 확인
    if (!item.getCart().getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("해당 장바구니 항목에 대한 권한이 없습니다.");
    }

    // TODO: 재고 확인 등 비즈니스 로직 추가

    item.setQuantity(newQuantity);

    CartItem updatedItem = cartItemRepository.save(item);
    return CartItemResponse.fromEntity(updatedItem);
  }

  /**
   * 장바구니에서 특정 항목을 삭제합니다.
   * @param userId 현재 로그인한 사용자 ID
   * @param cartItemId 삭제할 장바구니 항목 ID
   */
  public void removeItem(Long userId, Long cartItemId) {
    // 장바구니 항목 조회
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new EntityNotFoundException("장바구니 항목을 찾을 수 없습니다."));

    // 해당 항목이 현재 로그인한 사용자의 장바구니에 속하는지 확인
    if (!cartItem.getCart().getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("해당 장바구니 항목에 대한 권한이 없습니다.");
    }

    cartItemRepository.delete(cartItem);
  }

  /**
   * 특정 사용자의 장바구니를 전체 비웁니다.
   * @param userId 사용자 ID
   */
  public void clearCart(Long userId) {
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("장바구니를 찾을 수 없습니다."));

    // Cart 엔티티에 cascade = CascadeType.ALL, orphanRemoval = true 설정이 되어 있다면
    // 아래와 같이 List를 비우는 것만으로 DB에서 CartItem이 모두 삭제됩니다.
    cart.getCartItems().clear();
    cartRepository.save(cart);

    // 또는 CartItemRepository에서 deleteByCartId(cart.getId())와 같은 벌크 삭제 메서드를 사용할 수도 있습니다.
  }

}