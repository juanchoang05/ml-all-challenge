package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SellerDto(
    Long id,
    String nickname,
    LocalDateTime registrationDate,
    String firstName,
    String lastName,
    String countryId,
    String email,
    IdentificationDto identification,
    AddressDto address,
    PhoneDto phone,
    PhoneDto alternativePhone,
    String userType,
    List<String> tags,
    String logo,
    Integer points,
    String siteId,
    String permalink,
    SellerReputationDto sellerReputation,
    BuyerReputationDto buyerReputation,
    StatusDto status
) {
    @Builder
    public record IdentificationDto(
        String type,
        String number
    ) {}

    @Builder
    public record AddressDto(
        String city,
        String state,
        String zipCode
    ) {}

    @Builder
    public record PhoneDto(
        String areaCode,
        String number,
        String extension,
        Boolean verified
    ) {}

    @Builder
    public record SellerReputationDto(
        String powerSellerStatus,
        String levelId,
        MetricsDto metrics,
        TransactionsDto transactions
    ) {}

    @Builder
    public record MetricsDto(
        CancellationsDto cancellations,
        ClaimsDto claims,
        DelayedHandlingTimeDto delayedHandlingTime,
        SalesDto sales
    ) {}

    @Builder
    public record CancellationsDto(
        String period,
        Double rate,
        Integer value
    ) {}

    @Builder
    public record ClaimsDto(
        String period,
        Double rate,
        Integer value
    ) {}

    @Builder
    public record DelayedHandlingTimeDto(
        String period,
        Double rate,
        Integer value
    ) {}

    @Builder
    public record SalesDto(
        String period,
        Integer completed
    ) {}

    @Builder
    public record TransactionsDto(
        Integer canceled,
        Integer completed,
        String period,
        RatingsDto ratings,
        Integer total
    ) {}

    @Builder
    public record RatingsDto(
        Integer negative,
        Integer neutral,
        Integer positive
    ) {}

    @Builder
    public record BuyerReputationDto(
        Integer canceledTransactions,
        List<String> tags,
        BuyerTransactionsDto transactions
    ) {}

    @Builder
    public record BuyerTransactionsDto(
        CanceledDto canceled,
        Integer completed,
        NotYetRatedDto notYetRated,
        String period,
        Integer total,
        UnratedDto unrated
    ) {}

    @Builder
    public record CanceledDto(
        Integer paid,
        Integer total
    ) {}

    @Builder
    public record NotYetRatedDto(
        Integer paid,
        Integer total,
        Integer units
    ) {}

    @Builder
    public record UnratedDto(
        Integer paid,
        Integer total
    ) {}

    @Builder
    public record StatusDto(
        String siteStatus,
        ListDto list,
        BuyDto buy,
        SellDto sell,
        BillingDto billing,
        String mercadoenvios,
        Boolean mercadopagoTcAccepted,
        String mercadopagoAccountType,
        Boolean immediatePayment
    ) {}

    @Builder
    public record ListDto(
        Boolean allow,
        List<String> codes,
        ImmediatePaymentDto immediatePayment
    ) {}

    @Builder
    public record BuyDto(
        Boolean allow,
        List<String> codes,
        ImmediatePaymentDto immediatePayment
    ) {}

    @Builder
    public record SellDto(
        Boolean allow,
        List<String> codes,
        ImmediatePaymentDto immediatePayment
    ) {}

    @Builder
    public record BillingDto(
        Boolean allow,
        List<String> codes
    ) {}

    @Builder
    public record ImmediatePaymentDto(
        List<String> reasons,
        Boolean required
    ) {}
}
