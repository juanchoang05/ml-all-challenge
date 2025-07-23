package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PaymentMethodDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentMethodPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentMethodPersistencePort paymentMethodPersistencePort;
    
    @Mock
    private PaymentApiPort paymentApiPort;
    
    private PaymentService paymentService;
    
    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentMethodPersistencePort, paymentApiPort);
    }
    
    @Test
    void getPaymentMethodsBySite_ShouldReturnPaymentMethods() {
        // Given
        String siteId = "MLA";
        List<PaymentMethodResponse> mockResponse = Arrays.asList(
            PaymentMethodResponse.builder()
                .id("visa")
                .name("Visa")
                .paymentTypeId("credit_card")
                .build(),
            PaymentMethodResponse.builder()
                .id("master")
                .name("Mastercard")
                .paymentTypeId("credit_card")
                .build()
        );
        when(paymentApiPort.getPaymentMethodsBySite(siteId)).thenReturn(mockResponse);
        
        // When
        List<PaymentMethodResponse> result = paymentService.getPaymentMethodsBySite(siteId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("visa", result.get(0).id());
        assertEquals("Visa", result.get(0).name());
        assertEquals("credit_card", result.get(0).paymentTypeId());
        verify(paymentApiPort).getPaymentMethodsBySite(siteId);
    }
    
    @Test
    void getPaymentMethodsBySite_WhenNoPaymentMethods_ShouldReturnEmptyList() {
        // Given
        String siteId = "MLA";
        when(paymentApiPort.getPaymentMethodsBySite(siteId)).thenReturn(Collections.emptyList());
        
        // When
        List<PaymentMethodResponse> result = paymentService.getPaymentMethodsBySite(siteId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentApiPort).getPaymentMethodsBySite(siteId);
    }
    
    @Test
    void getInstallmentOptions_ShouldReturnInstallmentOptions() {
        // Given
        String siteId = "MLA";
        String amount = "1000";
        String paymentMethodId = "visa";
        List<PaymentMethodResponse> mockResponse = Arrays.asList(
            PaymentMethodResponse.builder()
                .id("visa_3")
                .name("3 cuotas sin interés")
                .paymentTypeId("credit_card")
                .build(),
            PaymentMethodResponse.builder()
                .id("visa_6")
                .name("6 cuotas con interés")
                .paymentTypeId("credit_card")
                .build()
        );
        when(paymentApiPort.getInstallmentOptions(siteId, amount, paymentMethodId)).thenReturn(mockResponse);
        
        // When
        List<PaymentMethodResponse> result = paymentService.getInstallmentOptions(siteId, amount, paymentMethodId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("visa_3", result.get(0).id());
        assertEquals("3 cuotas sin interés", result.get(0).name());
        verify(paymentApiPort).getInstallmentOptions(siteId, amount, paymentMethodId);
    }
    
    @Test
    void processPayment_ShouldProcessAndReturnPaymentResponse() {
        // Given
        PaymentRequest request = PaymentRequest.builder()
            .paymentMethodId("visa")
            .transactionAmount("1000.0")
            .description("Test payment")
            .payerId("PAYER-123")
            .token("TOKEN-456")
            .installments(1)
            .issuerId("ISSUER-789")
            .build();
        PaymentMethodResponse expectedResponse = PaymentMethodResponse.builder()
            .id("PAYMENT-456")
            .name("Payment Processed")
            .paymentTypeId("credit_card")
            .status("approved")
            .build();
        when(paymentApiPort.processPayment(request)).thenReturn(expectedResponse);
        
        // When
        PaymentMethodResponse result = paymentService.processPayment(request);
        
        // Then
        assertNotNull(result);
        assertEquals("PAYMENT-456", result.id());
        assertEquals("Payment Processed", result.name());
        assertEquals("approved", result.status());
        verify(paymentApiPort).processPayment(request);
    }
    
    @Test
    void getPaymentById_WhenPaymentExists_ShouldReturnPayment() {
        // Given
        String paymentId = "PAYMENT-123";
        List<PaymentMethodDto> mockPayments = Arrays.asList(
            createMockPaymentMethodDto(paymentId, "Visa Payment"),
            createMockPaymentMethodDto("PAYMENT-456", "Master Payment")
        );
        when(paymentMethodPersistencePort.findAll()).thenReturn(mockPayments);
        
        // When
        PaymentMethodResponse result = paymentService.getPaymentById(paymentId);
        
        // Then
        assertNotNull(result);
        assertEquals(paymentId, result.id());
        assertEquals("Visa Payment", result.name());
        assertEquals("credit_card", result.paymentTypeId());
        verify(paymentMethodPersistencePort).findAll();
    }
    
    @Test
    void getPaymentById_WhenPaymentNotFound_ShouldThrowException() {
        // Given
        String paymentId = "NON_EXISTENT";
        List<PaymentMethodDto> mockPayments = Arrays.asList(
            createMockPaymentMethodDto("PAYMENT-123", "Visa Payment")
        );
        when(paymentMethodPersistencePort.findAll()).thenReturn(mockPayments);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> paymentService.getPaymentById(paymentId));
        assertEquals("Payment not found with id: NON_EXISTENT", exception.getMessage());
        verify(paymentMethodPersistencePort).findAll();
    }
    
    @Test
    void getPaymentById_WhenNoPayments_ShouldThrowException() {
        // Given
        String paymentId = "PAYMENT-123";
        when(paymentMethodPersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> paymentService.getPaymentById(paymentId));
        assertEquals("Payment not found with id: PAYMENT-123", exception.getMessage());
        verify(paymentMethodPersistencePort).findAll();
    }
    
    @Test
    void mapToResponse_ShouldMapAllFields() {
        // Given
        String paymentId = "PAYMENT-COMPLETE";
        PaymentMethodDto paymentDto = createCompletePaymentMethodDto(paymentId);
        when(paymentMethodPersistencePort.findAll()).thenReturn(Arrays.asList(paymentDto));
        
        // When
        PaymentMethodResponse result = paymentService.getPaymentById(paymentId);
        
        // Then
        assertNotNull(result);
        assertEquals(paymentId, result.id());
        assertEquals("Complete Payment Method", result.name());
        assertEquals("credit_card", result.paymentTypeId());
        assertEquals("approved", result.status());
        assertEquals("https://secure.example.com/thumbnail.jpg", result.secureThumbnail());
        assertEquals("https://example.com/thumbnail.jpg", result.thumbnail());
        assertEquals("immediate", result.deferredCapture());
    }
    
    @Test
    void mapToResponse_WithNullFields_ShouldHandleNulls() {
        // Given
        String paymentId = "PAYMENT-NULL";
        PaymentMethodDto paymentDto = PaymentMethodDto.builder()
            .id(paymentId)
            .name("Payment with Nulls")
            .paymentTypeId("credit_card")
            .status(null)
            .secureThumbnail(null)
            .thumbnail(null)
            .deferredCapture(null)
            .build();
        when(paymentMethodPersistencePort.findAll()).thenReturn(Arrays.asList(paymentDto));
        
        // When
        PaymentMethodResponse result = paymentService.getPaymentById(paymentId);
        
        // Then
        assertNotNull(result);
        assertEquals(paymentId, result.id());
        assertEquals("Payment with Nulls", result.name());
        assertEquals("credit_card", result.paymentTypeId());
        assertNull(result.status());
        assertNull(result.secureThumbnail());
        assertNull(result.thumbnail());
        assertNull(result.deferredCapture());
    }
    
    @Test
    void getPaymentMethodsBySite_WithNullSiteId_ShouldCallApi() {
        // Given
        String siteId = null;
        when(paymentApiPort.getPaymentMethodsBySite(siteId)).thenReturn(Collections.emptyList());
        
        // When
        List<PaymentMethodResponse> result = paymentService.getPaymentMethodsBySite(siteId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentApiPort).getPaymentMethodsBySite(siteId);
    }
    
    private PaymentMethodDto createMockPaymentMethodDto(String id, String name) {
        return PaymentMethodDto.builder()
            .id(id)
            .name(name)
            .paymentTypeId("credit_card")
            .status("active")
            .secureThumbnail("https://secure.example.com/thumbnail.jpg")
            .thumbnail("https://example.com/thumbnail.jpg")
            .deferredCapture("immediate")
            .build();
    }
    
    private PaymentMethodDto createCompletePaymentMethodDto(String id) {
        return PaymentMethodDto.builder()
            .id(id)
            .name("Complete Payment Method")
            .paymentTypeId("credit_card")
            .status("approved")
            .secureThumbnail("https://secure.example.com/thumbnail.jpg")
            .thumbnail("https://example.com/thumbnail.jpg")
            .deferredCapture("immediate")
            .build();
    }
}
