package com.nowayback.order.product.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {

    @Id
    private UUID id;

    private String name;
    private int stock; // 재고

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.stock -= quantity;
    }
}

