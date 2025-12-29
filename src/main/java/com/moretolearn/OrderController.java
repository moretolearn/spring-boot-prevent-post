package com.moretolearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RedisIdempotencyService idempotency;
    private final OrderService orderService;

    public OrderController(
            RedisIdempotencyService idempotency,
            OrderService orderService) {
        this.idempotency = idempotency;
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse create(
            @RequestHeader("Idempotency-Key") String key,
            @RequestBody OrderRequest request) {

        return idempotency.execute(
            key,
            () -> orderService.createOrder(request),
            OrderResponse.class
        );
    }
}

