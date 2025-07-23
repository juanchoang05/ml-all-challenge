package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;

public interface OrderApiPort {
    
    OrderResponse createOrder(OrderRequest request);
    
    OrderResponse updateOrderStatus(String orderId, String status);
}
