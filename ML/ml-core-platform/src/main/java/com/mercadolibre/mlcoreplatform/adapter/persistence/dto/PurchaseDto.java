package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PurchaseDto(
    String id,
    LocalDateTime dateCreated,
    LocalDateTime dateClosed,
    LocalDateTime lastUpdated,
    String status,
    StatusDetailDto statusDetail,
    String currencyId,
    List<OrderItemDto> orderItems,
    Double totalAmount,
    Double paidAmount,
    CouponDto coupon,
    LocalDateTime expirationDate,
    ShippingInfoDto shipping,
    List<PaymentInfoDto> payments,
    BuyerDto buyer,
    PurchaseSellerDto seller,
    List<String> tags,
    FeedbackDto feedback,
    ContextDto context
) {
    @Builder
    public record StatusDetailDto(
        String code,
        String description
    ) {}

    @Builder
    public record OrderItemDto(
        ItemDto item,
        Integer quantity,
        Double unitPrice,
        Double fullUnitPrice,
        String currencyId,
        LocalDateTime manufacturingEndingDate,
        Double saleFee,
        String listingTypeId
    ) {}

    @Builder
    public record ItemDto(
        String id,
        String title,
        String categoryId,
        String variationId,
        String sellerCustomField,
        List<String> variationAttributes,
        String warranty,
        String condition,
        String sellerSku,
        String globalPrice,
        String netWeight
    ) {}

    @Builder
    public record CouponDto(
        String id,
        Double amount
    ) {}

    @Builder
    public record ShippingInfoDto(
        Long id,
        String shipmentType,
        String status,
        LocalDateTime dateCreated,
        LocalDateTime lastUpdated,
        String trackingNumber,
        String trackingMethod,
        Integer serviceId,
        LocalDateTime dateFirstPrinted,
        CarrierInfoDto carrierInfo,
        List<ShippingItemDto> shippingItems,
        ShippingOptionDto shippingOption,
        ReceiverAddressDto receiverAddress
    ) {}

    @Builder
    public record CarrierInfoDto(
        String name,
        String logo
    ) {}

    @Builder
    public record ShippingItemDto(
        String id,
        String description,
        Integer quantity,
        String dimensions
    ) {}

    @Builder
    public record ShippingOptionDto(
        Long id,
        String name,
        Double cost,
        String currencyId,
        EstimatedDeliveryDto estimatedDelivery
    ) {}

    @Builder
    public record EstimatedDeliveryDto(
        LocalDateTime date
    ) {}

    @Builder
    public record ReceiverAddressDto(
        Long id,
        String addressLine,
        String streetName,
        String streetNumber,
        String comment,
        String zipCode,
        CityDto city,
        StateDto state,
        CountryDto country,
        Double latitude,
        Double longitude,
        String receiverName,
        String receiverPhone
    ) {}

    @Builder
    public record CityDto(
        String id,
        String name
    ) {}

    @Builder
    public record StateDto(
        String id,
        String name
    ) {}

    @Builder
    public record CountryDto(
        String id,
        String name
    ) {}

    @Builder
    public record PaymentInfoDto(
        Long id,
        String orderId,
        Long payerId,
        CollectorDto collector,
        String cardId,
        String siteId,
        String reason,
        String paymentMethodId,
        String paymentType,
        String status,
        String statusDetail,
        String currencyId,
        String description,
        Integer installments,
        String issuerId,
        Double totalPaidAmount,
        Double shippingCost,
        Double couponAmount,
        LocalDateTime dateCreated,
        LocalDateTime dateLastUpdated,
        LocalDateTime dateApproved,
        String authorizationCode,
        Boolean captured,
        Boolean liveMode,
        String operationType
    ) {}

    @Builder
    public record CollectorDto(
        Long id
    ) {}

    @Builder
    public record BuyerDto(
        Long id,
        String nickname,
        String email,
        PhoneDto phone,
        String firstName,
        String lastName
    ) {}

    @Builder
    public record PhoneDto(
        String areaCode,
        String number
    ) {}

    @Builder
    public record PurchaseSellerDto(
        Long id,
        String nickname,
        String email,
        PhoneDto phone,
        String firstName,
        String lastName
    ) {}

    @Builder
    public record FeedbackDto(
        PurchaseFeedbackDto purchase,
        SaleFeedbackDto sale
    ) {}

    @Builder
    public record PurchaseFeedbackDto(
        Integer rating,
        String message
    ) {}

    @Builder
    public record SaleFeedbackDto(
        Integer rating,
        String message
    ) {}

    @Builder
    public record ContextDto(
        String channel,
        String site,
        List<String> flows,
        String application
    ) {}
}
