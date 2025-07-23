package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;

import java.util.List;

public interface OrderUseCase {
    
    List<OrderResponse> getAllOrders(int offset, int limit);
    
    OrderResponse getOrderById(String orderId);
    
    OrderResponse createOrder(OrderRequest request);
    
    OrderResponse updateOrderStatus(String orderId, String status);
    
    List<OrderResponse> getOrdersByUser(String userId, int offset, int limit);
}
