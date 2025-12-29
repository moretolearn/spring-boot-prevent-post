package com.moretolearn;

public record OrderRequest(
		Long userId,
        String requestId,
        Long productId,
        Integer quantity
		) 
{}
