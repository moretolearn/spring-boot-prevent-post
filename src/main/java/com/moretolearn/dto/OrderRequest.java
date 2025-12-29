package com.moretolearn.dto;

public record OrderRequest(
		Long userId,
        String requestId,
        Long productId,
        Integer quantity
		) 
{}
