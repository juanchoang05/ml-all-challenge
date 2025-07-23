package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request;

import lombok.Builder;

@Builder
public record ProductSearchRequest(
        String query,
        String categoryId,
        String siteId,
        String sortBy,
        String condition,
        Double minPrice,
        Double maxPrice,
        Integer limit,
        Integer offset,
        String shippingType,
        String paymentMethod,
        String location
) {
}
