package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ShippingDto(
    String id,
    String name,
    String displayName,
    String currencyId,
    Double listCost,
    Double cost,
    FreeShippingDto freeShipping,
    String tracksShipmentsStatus,
    EstimatedScheduleLimitDto estimatedScheduleLimit,
    EstimatedDeliveryDto estimatedDelivery,
    Integer speedRanking,
    String deliveryType,
    Long shippingMethodId,
    CoverageDto coverage,
    CompanyDto company,
    HandlingTimeDto handlingTime,
    List<PickupPointDto> pickupPoints
) {
    @Builder
    public record FreeShippingDto(
        Boolean flag,
        RuleDto rule
    ) {}

    @Builder
    public record RuleDto(
        String freeMode,
        Long value
    ) {}

    @Builder
    public record EstimatedScheduleLimitDto(
        LocalDateTime date
    ) {}

    @Builder
    public record EstimatedDeliveryDto(
        LocalDateTime date,
        String unit,
        OffsetDto offset
    ) {}

    @Builder
    public record OffsetDto(
        Integer date,
        Integer shipping
    ) {}

    @Builder
    public record CoverageDto(
        Boolean allCountry,
        SpecificPlacesDto specificPlaces
    ) {}

    @Builder
    public record SpecificPlacesDto(
        List<String> included,
        List<String> excluded
    ) {}

    @Builder
    public record CompanyDto(
        String id,
        String name,
        String logo
    ) {}

    @Builder
    public record HandlingTimeDto(
        Boolean excludeWeekends,
        String unit,
        Integer value
    ) {}

    @Builder
    public record PickupPointDto(
        String id,
        String name,
        String address,
        String city,
        String state,
        String zipCode,
        String phone,
        String hours,
        Double latitude,
        Double longitude
    ) {}
}
