package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.OrderApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Component
public class OrderApiAdapter implements OrderApiPort {

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // Mock implementation for testing
        return OrderResponse.builder()
                .id("ORDER-" + System.currentTimeMillis())
                .status("created")
                .dateCreated(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .currency("ARS")
                .totalAmount(BigDecimal.valueOf(1000.0))
                .build();
    }

    @Override
    public OrderResponse updateOrderStatus(String orderId, String status) {
        // Mock implementation for testing
        return OrderResponse.builder()
                .id(orderId)
                .status(status)
                .dateCreated(LocalDateTime.now().minusHours(1))
                .lastUpdated(LocalDateTime.now())
                .currency("ARS")
                .totalAmount(BigDecimal.valueOf(1000.0))
                .build();
    }
}
