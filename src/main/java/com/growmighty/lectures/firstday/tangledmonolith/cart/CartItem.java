package com.growmighty.lectures.firstday.tangledmonolith.cart;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    static CartItem create(Long productId, int quantity) {

        validateQuantity(quantity);
        CartItem cartItem = new CartItem();
        cartItem.productId = productId;
        cartItem.quantity = quantity;
        return cartItem;
    }

    void assignCart(Cart cart) {
        this.cart = cart;
    }

    void increase(int amount) {                 // Cart의 합산용
        validateQuantity(amount);
        this.quantity += amount;
    }

    void changeQuantity(int quantity) {         // Cart.changeQuantity용
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다: " + quantity);
        }
    }
}
