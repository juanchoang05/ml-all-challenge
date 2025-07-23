package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record CategoryResponse(
        String id,
        String name,
        String picture,
        String permalink,
        Integer totalItemsInThisCategory,
        List<PathFromRootResponse> pathFromRoot,
        List<CategoryResponse> childrenCategories,
        List<AttributeResponse> attributes,
        List<String> attributeTypes,
        SettingsResponse settings
) {
    
    @Builder
    public record PathFromRootResponse(
            String id,
            String name
    ) {}
    
    @Builder
    public record AttributeResponse(
            String id,
            String name,
            String type,
            Boolean required,
            List<ValueResponse> values,
            String hierarchy,
            String relevance,
            List<String> tags
    ) {}
    
    @Builder
    public record ValueResponse(
            String id,
            String name,
            Integer results
    ) {}
    
    @Builder
    public record SettingsResponse(
            Boolean adultContent,
            Boolean buyingAllowed,
            String buyingModes,
            String catalogDomain,
            String coverage,
            String currency,
            Boolean fragile,
            Boolean immediatePayment
    ) {}
}
