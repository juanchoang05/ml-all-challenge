package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.PaymentUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.PaymentController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentUseCase paymentUseCase;

    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentController = new PaymentController(paymentUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testGetPaymentMethodsBySite() throws Exception {
        // Arrange
        String siteId = "MLA";
        List<PaymentMethodResponse> mockResponse = Arrays.asList(
                PaymentMethodResponse.builder()
                        .id("visa")
                        .name("Visa")
                        .build(),
                PaymentMethodResponse.builder()
                        .id("mastercard")
                        .name("Mastercard")
                        .build()
        );

        when(paymentUseCase.getPaymentMethodsBySite(siteId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/payments/sites/{siteId}/payment_methods", siteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("visa"))
                .andExpect(jsonPath("$[0].name").value("Visa"))
                .andExpect(jsonPath("$[1].id").value("mastercard"))
                .andExpect(jsonPath("$[1].name").value("Mastercard"));
    }

    @Test
    void testGetInstallmentOptions() throws Exception {
        // Arrange
        String siteId = "MLA";
        String amount = "1000";
        String paymentMethodId = "visa";
        List<PaymentMethodResponse> mockResponse = Arrays.asList(
                PaymentMethodResponse.builder()
                        .id("installment_3")
                        .name("3 cuotas")
                        .build()
        );

        when(paymentUseCase.getInstallmentOptions(siteId, amount, paymentMethodId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/payments/sites/{siteId}/payment_methods/installments", siteId)
                        .param("amount", amount)
                        .param("paymentMethodId", paymentMethodId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("installment_3"));
    }

    @Test
    void testProcessPayment() throws Exception {
        // Arrange
        PaymentMethodResponse mockResponse = PaymentMethodResponse.builder()
                .id("payment_123")
                .name("Payment Processed")
                .build();

        when(paymentUseCase.processPayment(any(PaymentRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "amount": "1000",
                    "paymentMethodId": "visa",
                    "installments": 1
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("payment_123"))
                .andExpect(jsonPath("$.name").value("Payment Processed"));
    }

    @Test
    void testGetPaymentById() throws Exception {
        // Arrange
        String paymentId = "payment_123";
        PaymentMethodResponse mockResponse = PaymentMethodResponse.builder()
                .id(paymentId)
                .name("Payment Details")
                .build();

        when(paymentUseCase.getPaymentById(paymentId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/payments/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(paymentId))
                .andExpect(jsonPath("$.name").value("Payment Details"));
    }
}
