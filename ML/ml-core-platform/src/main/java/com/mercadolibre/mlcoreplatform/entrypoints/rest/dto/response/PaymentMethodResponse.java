package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record PaymentMethodResponse(
        String id,
        String name,
        String paymentTypeId,
        String status,
        String secureThumbnail,
        String thumbnail,
        String deferredCapture,
        List<SettingsResponse> settings,
        List<String> additionalInfoNeeded,
        BigDecimal minAllowedAmount,
        BigDecimal maxAllowedAmount,
        String accreditationTime,
        List<FinancialInstitutionResponse> financialInstitutions
) {
    
    @Builder
    public record SettingsResponse(
            String cardNumber,
            String bin,
            String securityCode
    ) {}
    
    @Builder
    public record FinancialInstitutionResponse(
            String id,
            String description
    ) {}
}
