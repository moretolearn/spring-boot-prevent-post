package com.moretolearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moretolearn.dto.OrderRequest;
import com.moretolearn.dto.OrderResponse;
import com.moretolearn.entity.Order;
import com.moretolearn.repository.OrderRepository;

import java.math.BigDecimal;

@Service
public class OrderService {

	@Autowired
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Core business logic.
     * This method MUST be:
     *  - transactional
     *  - free of idempotency logic
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order(
                request.userId(),
                request.requestId(),   // idempotency reference
                request.productId(),
                request.quantity(),
                calculateAmount(request),
                "CREATED"
        );

        try {
            Order saved = orderRepository.save(order);
            return new OrderResponse(
                    saved.getUserId(),
                    saved.getStatus(),
                    saved.getAmount()
            );

        } catch (DataIntegrityViolationException e) {
            // DB-level duplicate protection
            throw new RuntimeException(
                    "Duplicate order request detected"
            );
        }
    }

    private BigDecimal calculateAmount(OrderRequest request) {
        // Example calculation (replace with pricing service)
        return BigDecimal.valueOf(request.quantity()).multiply(
                BigDecimal.valueOf(1000)
        );
    }
}

