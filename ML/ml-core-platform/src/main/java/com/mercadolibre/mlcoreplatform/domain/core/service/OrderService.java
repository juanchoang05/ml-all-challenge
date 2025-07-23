package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PurchaseDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.OrderUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.OrderApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.PurchasePersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {
    
    private final PurchasePersistencePort purchasePersistencePort;
    private final OrderApiPort orderApiPort;
    
    @Override
    public List<OrderResponse> getAllOrders(int offset, int limit) {
        List<PurchaseDto> purchases = purchasePersistencePort.findAll();
        return purchases.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToOrderResponse)
                .toList();
    }
    
    @Override
    public OrderResponse getOrderById(String orderId) {
        List<PurchaseDto> purchases = purchasePersistencePort.findAll();
        return purchases.stream()
                .filter(p -> p.id().equals(orderId))
                .findFirst()
                .map(this::mapToOrderResponse)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        return orderApiPort.createOrder(request);
    }
    
    @Override
    public OrderResponse updateOrderStatus(String orderId, String status) {
        return orderApiPort.updateOrderStatus(orderId, status);
    }
    
    @Override
    public List<OrderResponse> getOrdersByUser(String userId, int offset, int limit) {
        List<PurchaseDto> purchases = purchasePersistencePort.findByBuyerId(Long.parseLong(userId));
        return purchases.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToOrderResponse)
                .toList();
    }
    
    private OrderResponse mapToOrderResponse(PurchaseDto purchaseDto) {
        return OrderResponse.builder()
                .id(purchaseDto.id())
                .status(purchaseDto.status())
                .dateCreated(purchaseDto.dateCreated())
                .lastUpdated(purchaseDto.lastUpdated())
                .currency(purchaseDto.currencyId())
                .totalAmount(purchaseDto.totalAmount() != null ? 
                    java.math.BigDecimal.valueOf(purchaseDto.totalAmount()) : null)
                .build();
    }
}
