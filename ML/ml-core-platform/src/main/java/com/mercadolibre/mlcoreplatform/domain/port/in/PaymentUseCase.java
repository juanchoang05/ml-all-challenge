package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentUseCase {
    
    List<PaymentMethodResponse> getPaymentMethodsBySite(String siteId);
    
    List<PaymentMethodResponse> getInstallmentOptions(String siteId, String amount, String paymentMethodId);
    
    PaymentMethodResponse processPayment(PaymentRequest request);
    
    PaymentMethodResponse getPaymentById(String paymentId);
}
