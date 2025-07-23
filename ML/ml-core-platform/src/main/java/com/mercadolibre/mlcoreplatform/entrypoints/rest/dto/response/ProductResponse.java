package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductResponse(
        String id,
        String title,
        String categoryId,
        BigDecimal price,
        String currencyId,
        Integer availableQuantity,
        String buyingMode,
        String listingTypeId,
        String condition,
        String permalink,
        String thumbnailId,
        String pictureId,
        LocalDateTime dateCreated,
        LocalDateTime lastUpdated,
        String sellerId,
        String warranty,
        List<String> pictures,
        List<AttributeResponse> attributes,
        ShippingResponse shipping,
        SellerResponse seller
) {
    
    @Builder
    public record AttributeResponse(
            String id,
            String name,
            String valueId,
            String valueName,
            String valueStruct
    ) {}
    
    @Builder
    public record ShippingResponse(
            String mode,
            Boolean freeShipping,
            String logisticType,
            List<String> tags
    ) {}
    
    @Builder
    public record SellerResponse(
            String id,
            String nickname,
            Integer powerSellerStatus,
            String carDealer,
            String realEstateAgency
    ) {}
}
