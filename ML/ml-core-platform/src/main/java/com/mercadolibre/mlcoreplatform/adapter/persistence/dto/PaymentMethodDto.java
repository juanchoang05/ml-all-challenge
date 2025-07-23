package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentMethodDto(
    String id,
    String name,
    String paymentTypeId,
    String status,
    String secureThumbnail,
    String thumbnail,
    String deferredCapture,
    List<SettingDto> settings,
    List<String> additionalInfoNeeded,
    Long minAllowedAmount,
    Long maxAllowedAmount,
    Integer accreditationTime,
    List<FinancialInstitutionDto> financialInstitutions,
    List<String> processingModes
) {
    @Builder
    public record SettingDto(
        BinDto bin,
        CardNumberDto cardNumber,
        SecurityCodeDto securityCode
    ) {}

    @Builder
    public record BinDto(
        String pattern,
        String installmentsPattern,
        String exclusionPattern
    ) {}

    @Builder
    public record CardNumberDto(
        String validation,
        Integer length
    ) {}

    @Builder
    public record SecurityCodeDto(
        Integer length,
        String cardLocation,
        String mode
    ) {}

    @Builder
    public record FinancialInstitutionDto(
        String id,
        String description
    ) {}
}
