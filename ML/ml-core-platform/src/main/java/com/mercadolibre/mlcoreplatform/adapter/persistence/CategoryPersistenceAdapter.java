package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.CategoryDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Object>> categoriesData;

    public CategoryPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        
        ClassPathResource resource = new ClassPathResource("categories.json");
        this.categoriesData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, Map<String, Object>>>() {}
        );
    }

    @Override
    public Optional<CategoryDto> findById(String categoryId) {
        Map<String, Object> categoryData = categoriesData.get(categoryId);
        if (categoryData == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapToCategoryDto(categoryData));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoriesData.values().stream()
                .map(this::mapToCategoryDto)
                .toList();
    }

    @Override
    public List<CategoryDto> findByParentId(String parentId) {
        return categoriesData.values().stream()
                .filter(data -> hasParentId(data, parentId))
                .map(this::mapToCategoryDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private boolean hasParentId(Map<String, Object> categoryData, String parentId) {
        if (parentId == null) {
            return false;
        }
        
        List<Map<String, Object>> pathFromRoot = (List<Map<String, Object>>) categoryData.get("path_from_root");
        if (pathFromRoot == null || pathFromRoot.isEmpty()) {
            return false;
        }
        
        // Check if parentId is in the path (excluding the last element which is the category itself)
        for (int i = 0; i < pathFromRoot.size() - 1; i++) {
            if (parentId.equals(pathFromRoot.get(i).get("id"))) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private CategoryDto mapToCategoryDto(Map<String, Object> data) {
        return CategoryDto.builder()
                .id((String) data.get("id"))
                .name((String) data.get("name"))
                .picture((String) data.get("picture"))
                .permalink((String) data.get("permalink"))
                .totalItemsInThisCategory(data.get("total_items_in_this_category") != null ? 
                    ((Number) data.get("total_items_in_this_category")).longValue() : null)
                .pathFromRoot(mapPathFromRoot((List<Map<String, Object>>) data.get("path_from_root")))
                .childrenCategories(mapChildrenCategories((List<Map<String, Object>>) data.get("children_categories")))
                .settings(mapSettings((Map<String, Object>) data.get("settings")))
                .build();
    }

    private List<CategoryDto.PathFromRootDto> mapPathFromRoot(List<Map<String, Object>> pathFromRoot) {
        if (pathFromRoot == null) return List.of();
        return pathFromRoot.stream()
                .map(path -> CategoryDto.PathFromRootDto.builder()
                        .id((String) path.get("id"))
                        .name((String) path.get("name"))
                        .build())
                .collect(Collectors.toList());
    }

    private List<CategoryDto.ChildrenCategoryDto> mapChildrenCategories(List<Map<String, Object>> childrenCategories) {
        if (childrenCategories == null) return List.of();
        return childrenCategories.stream()
                .map(child -> CategoryDto.ChildrenCategoryDto.builder()
                        .id((String) child.get("id"))
                        .name((String) child.get("name"))
                        .totalItemsInThisCategory(child.get("total_items_in_this_category") != null ? 
                            ((Number) child.get("total_items_in_this_category")).longValue() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private CategoryDto.SettingsDto mapSettings(Map<String, Object> settings) {
        if (settings == null) return null;
        return CategoryDto.SettingsDto.builder()
                .adultContent((Boolean) settings.get("adult_content"))
                .buyingAllowed((Boolean) settings.get("buying_allowed"))
                .buyingModes((List<String>) settings.get("buying_modes"))
                .catalogDomain((String) settings.get("catalog_domain"))
                .coverageAreas((String) settings.get("coverage_areas"))
                .currency((String) settings.get("currency"))
                .immediatePayment((String) settings.get("immediate_payment"))
                .itemConditions((List<String>) settings.get("item_conditions"))
                .itemsReviewsAllowed((Boolean) settings.get("items_reviews_allowed"))
                .listingAllowed((Boolean) settings.get("listing_allowed"))
                .maxDescriptionLength(settings.get("max_description_length") != null ? 
                    ((Number) settings.get("max_description_length")).longValue() : null)
                .maxPicturesPerItem(settings.get("max_pictures_per_item") != null ? 
                    ((Number) settings.get("max_pictures_per_item")).intValue() : null)
                .maxPicturesPerItemVar(settings.get("max_pictures_per_item_var") != null ? 
                    ((Number) settings.get("max_pictures_per_item_var")).intValue() : null)
                .maxSubTitleLength(settings.get("max_sub_title_length") != null ? 
                    ((Number) settings.get("max_sub_title_length")).intValue() : null)
                .maxTitleLength(settings.get("max_title_length") != null ? 
                    ((Number) settings.get("max_title_length")).intValue() : null)
                .maximumPrice(settings.get("maximum_price") != null ? 
                    ((Number) settings.get("maximum_price")).longValue() : null)
                .maximumPriceCurrency((String) settings.get("maximum_price_currency"))
                .minimumPrice(settings.get("minimum_price") != null ? 
                    ((Number) settings.get("minimum_price")).longValue() : null)
                .minimumPriceCurrency((String) settings.get("minimum_price_currency"))
                .mirrorCategory((String) settings.get("mirror_category"))
                .mirrorMasterCategory((String) settings.get("mirror_master_category"))
                .mirrorSlaveCategories((List<String>) settings.get("mirror_slave_categories"))
                .price((String) settings.get("price"))
                .reservationAllowed((String) settings.get("reservation_allowed"))
                .restrictions((List<String>) settings.get("restrictions"))
                .roundedAddress((Boolean) settings.get("rounded_address"))
                .sellerContact((String) settings.get("seller_contact"))
                .shippingOptions((List<String>) settings.get("shipping_options"))
                .shippingProfile((String) settings.get("shipping_profile"))
                .showContactInformation((Boolean) settings.get("show_contact_information"))
                .simpleShipping((String) settings.get("simple_shipping"))
                .stock((String) settings.get("stock"))
                .subVertical((String) settings.get("sub_vertical"))
                .subscribable((Boolean) settings.get("subscribable"))
                .tags((List<String>) settings.get("tags"))
                .vertical((String) settings.get("vertical"))
                .vipSubdomain((String) settings.get("vip_subdomain"))
                .buyerProtectionPrograms((List<String>) settings.get("buyer_protection_programs"))
                .status((String) settings.get("status"))
                .build();
    }
}
