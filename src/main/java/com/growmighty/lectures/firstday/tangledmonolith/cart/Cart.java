package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }

    public void addItem(Long productId, int quantity) {
        findItem(productId).ifPresentOrElse(
            item -> item.increase(quantity),
            () -> {
                CartItem item = CartItem.create(productId, quantity);
                item.assignCart(this);
                this.items.add(item);
            });
    }

    public void clear() {
        this.items.clear();
    }

    public void changeQuantity(Long productId, int quantity) {
        getItem(productId).changeQuantity(quantity);
    }

    public void removeItem(Long productId) {
        this.items.remove(getItem(productId));   // orphanRemoval = true라 DB에서도 삭제됨
    }

    private Optional<CartItem> findItem(Long productId) {
        return items.stream()
            .filter(i -> i.getProductId().equals(productId))
            .findFirst();
    }

    private CartItem getItem(Long productId) {
        return findItem(productId).orElseThrow(
            () -> new IllegalArgumentException("장바구니에 없는 상품입니다: " + productId));
    }
}
