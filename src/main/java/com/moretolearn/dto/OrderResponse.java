package com.moretolearn.dto;

import java.math.BigDecimal;

public record OrderResponse(
		Long orderId,
        String status,
        BigDecimal amount
		) {

}
