package com.mercadolibre.mlcoreplatform.domain.port.in.dto;

import lombok.Builder;

@Builder
public record SearchCriteria(
    String siteId,
    String query,
    String categoryId,
    String condition,
    Double minPrice,
    Double maxPrice,
    String shipping,
    String sort,
    int offset,
    int limit
) {}
