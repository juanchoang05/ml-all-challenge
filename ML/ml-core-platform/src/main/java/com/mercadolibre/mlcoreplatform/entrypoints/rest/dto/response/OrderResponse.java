package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(
        String id,
        String status,
        String statusDetail,
        LocalDateTime dateCreated,
        LocalDateTime dateCloseNegotiation,
        LocalDateTime lastUpdated,
        String currency,
        BigDecimal totalAmount,
        BuyerResponse buyer,
        SellerResponse seller,
        String paymentId,
        String shippingId,
        String feedbackStatus,
        String mediationId,
        String context,
        OrderItemResponse orderItem
) {
    
    @Builder
    public record BuyerResponse(
            String id,
            String nickname,
            String email,
            String phone
    ) {}
    
    @Builder
    public record SellerResponse(
            String id,
            String nickname,
            String email,
            String phone
    ) {}
    
    @Builder
    public record OrderItemResponse(
            String itemId,
            String categoryId,
            String title,
            BigDecimal unitPrice,
            Integer quantity,
            String variation,
            String currency,
            String condition
    ) {}
}
