package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDto(
    String id,
    String name,
    String picture,
    String permalink,
    Long totalItemsInThisCategory,
    List<PathFromRootDto> pathFromRoot,
    List<ChildrenCategoryDto> childrenCategories,
    SettingsDto settings
) {
    @Builder
    public record PathFromRootDto(
        String id,
        String name
    ) {}

    @Builder
    public record ChildrenCategoryDto(
        String id,
        String name,
        Long totalItemsInThisCategory
    ) {}

    @Builder
    public record SettingsDto(
        Boolean adultContent,
        Boolean buyingAllowed,
        List<String> buyingModes,
        String catalogDomain,
        String coverageAreas,
        String currency,
        String immediatePayment,
        List<String> itemConditions,
        Boolean itemsReviewsAllowed,
        Boolean listingAllowed,
        Long maxDescriptionLength,
        Integer maxPicturesPerItem,
        Integer maxPicturesPerItemVar,
        Integer maxSubTitleLength,
        Integer maxTitleLength,
        Long maximumPrice,
        String maximumPriceCurrency,
        Long minimumPrice,
        String minimumPriceCurrency,
        String mirrorCategory,
        String mirrorMasterCategory,
        List<String> mirrorSlaveCategories,
        String price,
        String reservationAllowed,
        List<String> restrictions,
        Boolean roundedAddress,
        String sellerContact,
        List<String> shippingOptions,
        String shippingProfile,
        Boolean showContactInformation,
        String simpleShipping,
        String stock,
        String subVertical,
        Boolean subscribable,
        List<String> tags,
        String vertical,
        String vipSubdomain,
        List<String> buyerProtectionPrograms,
        String status
    ) {}
}
