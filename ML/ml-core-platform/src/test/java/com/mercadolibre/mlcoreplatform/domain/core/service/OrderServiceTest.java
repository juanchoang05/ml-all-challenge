package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PurchaseDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.OrderApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.PurchasePersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PurchasePersistencePort purchasePersistencePort;
    
    @Mock
    private OrderApiPort orderApiPort;
    
    private OrderService orderService;
    
    @BeforeEach
    void setUp() {
        orderService = new OrderService(purchasePersistencePort, orderApiPort);
    }
    
    @Test
    void getAllOrders_ShouldReturnPaginatedOrders() {
        // Given
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto("ORDER-1", "confirmed"),
            createMockPurchaseDto("ORDER-2", "pending"),
            createMockPurchaseDto("ORDER-3", "cancelled")
        );
        when(purchasePersistencePort.findAll()).thenReturn(mockPurchases);
        
        // When
        List<OrderResponse> result = orderService.getAllOrders(0, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORDER-1", result.get(0).id());
        assertEquals("confirmed", result.get(0).status());
        assertEquals("ORDER-2", result.get(1).id());
        assertEquals("pending", result.get(1).status());
        verify(purchasePersistencePort).findAll();
    }
    
    @Test
    void getAllOrders_WithOffset_ShouldSkipCorrectNumber() {
        // Given
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto("ORDER-1", "confirmed"),
            createMockPurchaseDto("ORDER-2", "pending"),
            createMockPurchaseDto("ORDER-3", "cancelled")
        );
        when(purchasePersistencePort.findAll()).thenReturn(mockPurchases);
        
        // When
        List<OrderResponse> result = orderService.getAllOrders(1, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORDER-2", result.get(0).id());
        assertEquals("ORDER-3", result.get(1).id());
        verify(purchasePersistencePort).findAll();
    }
    
    @Test
    void getAllOrders_WhenNoPurchases_ShouldReturnEmptyList() {
        // Given
        when(purchasePersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<OrderResponse> result = orderService.getAllOrders(0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(purchasePersistencePort).findAll();
    }
    
    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrder() {
        // Given
        String orderId = "ORDER-1";
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto(orderId, "confirmed"),
            createMockPurchaseDto("ORDER-2", "pending")
        );
        when(purchasePersistencePort.findAll()).thenReturn(mockPurchases);
        
        // When
        OrderResponse result = orderService.getOrderById(orderId);
        
        // Then
        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals("confirmed", result.status());
        verify(purchasePersistencePort).findAll();
    }
    
    @Test
    void getOrderById_WhenOrderNotFound_ShouldThrowException() {
        // Given
        String orderId = "NON_EXISTENT";
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto("ORDER-1", "confirmed")
        );
        when(purchasePersistencePort.findAll()).thenReturn(mockPurchases);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> orderService.getOrderById(orderId));
        assertEquals("Order not found with id: NON_EXISTENT", exception.getMessage());
        verify(purchasePersistencePort).findAll();
    }
    
    @Test
    void createOrder_ShouldCallApiAndReturnResponse() {
        // Given
        OrderRequest request = OrderRequest.builder()
            .itemId("MLA123456")
            .quantity(1)
            .build();
        OrderResponse expectedResponse = OrderResponse.builder()
            .id("ORDER-NEW")
            .status("created")
            .build();
        when(orderApiPort.createOrder(request)).thenReturn(expectedResponse);
        
        // When
        OrderResponse result = orderService.createOrder(request);
        
        // Then
        assertNotNull(result);
        assertEquals("ORDER-NEW", result.id());
        assertEquals("created", result.status());
        verify(orderApiPort).createOrder(request);
    }
    
    @Test
    void updateOrderStatus_ShouldCallApiAndReturnResponse() {
        // Given
        String orderId = "ORDER-1";
        String newStatus = "confirmed";
        OrderResponse expectedResponse = OrderResponse.builder()
            .id(orderId)
            .status(newStatus)
            .build();
        when(orderApiPort.updateOrderStatus(orderId, newStatus)).thenReturn(expectedResponse);
        
        // When
        OrderResponse result = orderService.updateOrderStatus(orderId, newStatus);
        
        // Then
        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals(newStatus, result.status());
        verify(orderApiPort).updateOrderStatus(orderId, newStatus);
    }
    
    @Test
    void getOrdersByUser_ShouldReturnUserOrders() {
        // Given
        String userId = "123";
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto("ORDER-1", "confirmed"),
            createMockPurchaseDto("ORDER-2", "pending")
        );
        when(purchasePersistencePort.findByBuyerId(Long.parseLong(userId))).thenReturn(mockPurchases);
        
        // When
        List<OrderResponse> result = orderService.getOrdersByUser(userId, 0, 10);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORDER-1", result.get(0).id());
        assertEquals("ORDER-2", result.get(1).id());
        verify(purchasePersistencePort).findByBuyerId(123L);
    }
    
    @Test
    void getOrdersByUser_WithPagination_ShouldReturnLimitedResults() {
        // Given
        String userId = "123";
        List<PurchaseDto> mockPurchases = Arrays.asList(
            createMockPurchaseDto("ORDER-1", "confirmed"),
            createMockPurchaseDto("ORDER-2", "pending"),
            createMockPurchaseDto("ORDER-3", "cancelled")
        );
        when(purchasePersistencePort.findByBuyerId(Long.parseLong(userId))).thenReturn(mockPurchases);
        
        // When
        List<OrderResponse> result = orderService.getOrdersByUser(userId, 1, 1);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ORDER-2", result.get(0).id());
        verify(purchasePersistencePort).findByBuyerId(123L);
    }
    
    @Test
    void getOrdersByUser_WhenUserHasNoOrders_ShouldReturnEmptyList() {
        // Given
        String userId = "123";
        when(purchasePersistencePort.findByBuyerId(Long.parseLong(userId))).thenReturn(Collections.emptyList());
        
        // When
        List<OrderResponse> result = orderService.getOrdersByUser(userId, 0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(purchasePersistencePort).findByBuyerId(123L);
    }
    
    @Test
    void mapToOrderResponse_ShouldMapAllFields() {
        // Given
        PurchaseDto purchaseDto = createMockPurchaseDtoWithAllFields();
        when(purchasePersistencePort.findAll()).thenReturn(Arrays.asList(purchaseDto));
        
        // When
        OrderResponse result = orderService.getOrderById("ORDER-COMPLETE");
        
        // Then
        assertNotNull(result);
        assertEquals("ORDER-COMPLETE", result.id());
        assertEquals("confirmed", result.status());
        assertNotNull(result.dateCreated());
        assertNotNull(result.lastUpdated());
        assertEquals("ARS", result.currency());
        assertEquals(BigDecimal.valueOf(1500.0), result.totalAmount());
    }
    
    @Test
    void mapToOrderResponse_WithNullTotalAmount_ShouldHandleNull() {
        // Given
        PurchaseDto purchaseDto = PurchaseDto.builder()
            .id("ORDER-NULL")
            .status("pending")
            .dateCreated(LocalDateTime.now())
            .lastUpdated(LocalDateTime.now())
            .currencyId("ARS")
            .totalAmount(null)
            .build();
        when(purchasePersistencePort.findAll()).thenReturn(Arrays.asList(purchaseDto));
        
        // When
        OrderResponse result = orderService.getOrderById("ORDER-NULL");
        
        // Then
        assertNotNull(result);
        assertEquals("ORDER-NULL", result.id());
        assertNull(result.totalAmount());
    }
    
    private PurchaseDto createMockPurchaseDto(String id, String status) {
        return PurchaseDto.builder()
            .id(id)
            .status(status)
            .dateCreated(LocalDateTime.now())
            .lastUpdated(LocalDateTime.now())
            .currencyId("ARS")
            .totalAmount(1000.0)
            .build();
    }
    
    private PurchaseDto createMockPurchaseDtoWithAllFields() {
        return PurchaseDto.builder()
            .id("ORDER-COMPLETE")
            .status("confirmed")
            .dateCreated(LocalDateTime.of(2024, 1, 15, 10, 30))
            .lastUpdated(LocalDateTime.of(2024, 1, 15, 12, 0))
            .currencyId("ARS")
            .totalAmount(1500.0)
            .build();
    }
}
