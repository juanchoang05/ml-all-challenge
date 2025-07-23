package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentApiPort {
    
    List<PaymentMethodResponse> getPaymentMethodsBySite(String siteId);
    
    List<PaymentMethodResponse> getInstallmentOptions(String siteId, String amount, String paymentMethodId);
    
    PaymentMethodResponse processPayment(PaymentRequest request);
}
