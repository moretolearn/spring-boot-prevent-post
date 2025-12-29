package com.moretolearn.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "orders",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_order",
        columnNames = {"user_id", "request_id"}
    )
)
@Data
public class Order {

    // 🔑 Primary Key (MySQL AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 Who placed the order
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 🔁 Idempotency key (per user)
    @Column(name = "request_id", nullable = false, length = 64)
    private String requestId;

    // 📦 Product reference
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // 🔢 Quantity
    @Column(nullable = false)
    private Integer quantity;

    // 💰 Order amount
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // 📌 Order status
    @Column(nullable = false, length = 20)
    private String status;

    // ⏰ Created timestamp
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    /* --------------------
       Constructors
    -------------------- */

    protected Order() {
        // JPA only
    }

    public Order(
            Long userId,
            String requestId,
            Long productId,
            Integer quantity,
            BigDecimal amount,
            String status
    ) {
        this.userId = userId;
        this.requestId = requestId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    /* --------------------
       Getters & Setters
    -------------------- */

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

