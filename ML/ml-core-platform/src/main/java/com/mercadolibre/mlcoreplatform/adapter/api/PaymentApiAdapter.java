package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentApiAdapter implements PaymentApiPort {

    @Override
    public List<PaymentMethodResponse> getPaymentMethodsBySite(String siteId) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public List<PaymentMethodResponse> getInstallmentOptions(String siteId, String amount, String paymentMethodId) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public PaymentMethodResponse processPayment(PaymentRequest request) {
        // Mock implementation for testing
        return PaymentMethodResponse.builder()
                .id("payment-mock-" + System.currentTimeMillis())
                .name("Mock Payment")
                .paymentTypeId("credit_card")
                .status("approved")
                .build();
    }
}
