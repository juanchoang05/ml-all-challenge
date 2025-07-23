package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.OrderUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.OrderController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderUseCase orderUseCase;

    private OrderController orderController;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        // Arrange
        List<OrderResponse> mockResponse = Arrays.asList(
                OrderResponse.builder()
                        .id("ORDER123")
                        .status("confirmed")
                        .totalAmount(BigDecimal.valueOf(999.99))
                        .build(),
                OrderResponse.builder()
                        .id("ORDER456")
                        .status("paid")
                        .totalAmount(BigDecimal.valueOf(1299.99))
                        .build()
        );

        when(orderUseCase.getAllOrders(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/orders")
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("ORDER123"))
                .andExpect(jsonPath("$[0].status").value("confirmed"))
                .andExpect(jsonPath("$[1].id").value("ORDER456"))
                .andExpect(jsonPath("$[1].status").value("paid"));
    }

    @Test
    void testGetAllOrdersWithDefaultParams() throws Exception {
        // Arrange
        List<OrderResponse> mockResponse = Arrays.asList(
                OrderResponse.builder()
                        .id("ORDER123")
                        .status("confirmed")
                        .build()
        );

        when(orderUseCase.getAllOrders(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetOrderById() throws Exception {
        // Arrange
        String orderId = "ORDER123";
        OrderResponse mockResponse = OrderResponse.builder()
                .id(orderId)
                .status("confirmed")
                .totalAmount(BigDecimal.valueOf(999.99))
                .currency("ARS")
                .build();

        when(orderUseCase.getOrderById(orderId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("confirmed"))
                .andExpect(jsonPath("$.totalAmount").value(999.99))
                .andExpect(jsonPath("$.currency").value("ARS"));
    }

    @Test
    void testCreateOrder() throws Exception {
        // Arrange
        OrderResponse mockResponse = OrderResponse.builder()
                .id("ORDER123")
                .status("pending")
                .totalAmount(BigDecimal.valueOf(999.99))
                .currency("ARS")
                .build();

        when(orderUseCase.createOrder(any(OrderRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "buyerId": "USER123",
                    "sellerId": "SELLER456",
                    "itemId": "ITEM123",
                    "quantity": 1,
                    "paymentMethodId": "visa",
                    "shippingMode": "standard",
                    "shippingAddress": "Av. Corrientes 123, Buenos Aires"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("ORDER123"))
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.totalAmount").value(999.99));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        // Arrange
        String orderId = "ORDER123";
        String newStatus = "shipped";
        OrderResponse mockResponse = OrderResponse.builder()
                .id(orderId)
                .status(newStatus)
                .totalAmount(BigDecimal.valueOf(999.99))
                .build();

        when(orderUseCase.updateOrderStatus(orderId, newStatus)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/orders/{id}/status", orderId)
                        .param("status", newStatus))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value(newStatus));
    }

    @Test
    void testGetOrdersByUser() throws Exception {
        // Arrange
        String userId = "USER123";
        List<OrderResponse> mockResponse = Arrays.asList(
                OrderResponse.builder()
                        .id("ORDER123")
                        .status("confirmed")
                        .totalAmount(BigDecimal.valueOf(999.99))
                        .build(),
                OrderResponse.builder()
                        .id("ORDER456")
                        .status("delivered")
                        .totalAmount(BigDecimal.valueOf(1299.99))
                        .build()
        );

        when(orderUseCase.getOrdersByUser(userId, 0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/orders/user/{userId}", userId)
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("ORDER123"))
                .andExpect(jsonPath("$[0].status").value("confirmed"))
                .andExpect(jsonPath("$[1].id").value("ORDER456"))
                .andExpect(jsonPath("$[1].status").value("delivered"));
    }
}
