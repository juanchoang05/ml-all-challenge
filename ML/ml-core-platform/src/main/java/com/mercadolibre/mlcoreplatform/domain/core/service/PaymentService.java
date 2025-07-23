package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PaymentMethodDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.PaymentUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentMethodPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {
    
    private final PaymentMethodPersistencePort paymentMethodPersistencePort;
    private final PaymentApiPort paymentApiPort;
    
    @Override
    public List<PaymentMethodResponse> getPaymentMethodsBySite(String siteId) {
        return paymentApiPort.getPaymentMethodsBySite(siteId);
    }
    
    @Override
    public List<PaymentMethodResponse> getInstallmentOptions(String siteId, String amount, String paymentMethodId) {
        return paymentApiPort.getInstallmentOptions(siteId, amount, paymentMethodId);
    }
    
    @Override
    public PaymentMethodResponse processPayment(PaymentRequest request) {
        return paymentApiPort.processPayment(request);
    }
    
    @Override
    public PaymentMethodResponse getPaymentById(String paymentId) {
        List<PaymentMethodDto> payments = paymentMethodPersistencePort.findAll();
        return payments.stream()
                .filter(p -> p.id().equals(paymentId))
                .findFirst()
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
    }
    
    private PaymentMethodResponse mapToResponse(PaymentMethodDto paymentDto) {
        return PaymentMethodResponse.builder()
                .id(paymentDto.id())
                .name(paymentDto.name())
                .paymentTypeId(paymentDto.paymentTypeId())
                .status(paymentDto.status())
                .secureThumbnail(paymentDto.secureThumbnail())
                .thumbnail(paymentDto.thumbnail())
                .deferredCapture(paymentDto.deferredCapture())
                .build();
    }
}
