package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductDto(
    String id,
    String siteId,
    String title,
    String subtitle,
    Long sellerId,
    String categoryId,
    String officialStoreId,
    Double price,
    Double basePrice,
    Double originalPrice,
    String currencyId,
    Integer initialQuantity,
    Integer availableQuantity,
    Integer soldQuantity,
    String condition,
    String permalink,
    String thumbnail,
    String secureThumbnail,
    List<PictureDto> pictures,
    String videoId,
    List<DescriptionDto> descriptions,
    Boolean acceptsMercadopago,
    List<String> nonMercadoPagoPaymentMethods,
    ShippingDto shipping,
    SellerAddressDto sellerAddress,
    List<AttributeDto> attributes,
    List<String> warnings,
    String listingSource,
    List<VariationDto> variations,
    String status,
    String warranty,
    String catalogProductId,
    String domainId,
    String sellerContact,
    LocationDto location,
    GeolocationDto geolocation,
    List<String> coverageAreas,
    String sellerCustomField,
    String parentItemId,
    String differentialPricing,
    List<String> dealIds,
    Boolean automaticRelist,
    LocalDateTime dateCreated,
    LocalDateTime lastUpdated
) {
    @Builder
    public record PictureDto(
        String id,
        String url,
        String secureUrl,
        String size,
        String maxSize
    ) {}

    @Builder
    public record DescriptionDto(
        String id,
        String text
    ) {}

    @Builder
    public record ShippingDto(
        String mode,
        List<ShippingMethodDto> methods,
        List<String> tags,
        String dimensions,
        Boolean localPickUp,
        Boolean freeShipping,
        String logisticType,
        Boolean storePickUp
    ) {}

    @Builder
    public record ShippingMethodDto(
        String id,
        String name
    ) {}

    @Builder
    public record SellerAddressDto(
        CityDto city,
        StateDto state,
        CountryDto country,
        SearchLocationDto searchLocation,
        Long id
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
    public record SearchLocationDto(
        NeighborhoodDto neighborhood,
        CityDto city,
        StateDto state
    ) {}

    @Builder
    public record NeighborhoodDto(
        String id,
        String name
    ) {}

    @Builder
    public record AttributeDto(
        String id,
        String name,
        String valueId,
        String valueName,
        String valueStruct,
        List<AttributeValueDto> values,
        String attributeGroupId,
        String attributeGroupName
    ) {}

    @Builder
    public record AttributeValueDto(
        String id,
        String name,
        String struct
    ) {}

    @Builder
    public record VariationDto(
        String id,
        Double price,
        List<AttributeDto> attributes,
        Integer availableQuantity,
        Integer soldQuantity,
        List<PictureDto> pictures
    ) {}

    @Builder
    public record LocationDto() {}

    @Builder
    public record GeolocationDto(
        Double latitude,
        Double longitude
    ) {}
}
