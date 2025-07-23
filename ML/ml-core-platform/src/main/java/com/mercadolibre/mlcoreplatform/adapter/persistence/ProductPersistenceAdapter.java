package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ProductDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.ProductPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Object>> productsData;

    public ProductPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("products.json");
        this.productsData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, Map<String, Object>>>() {}
        );
    }

    @Override
    public Optional<ProductDto> findById(String productId) {
        Map<String, Object> productData = productsData.get(productId);
        if (productData == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapToProductDto(productData));
    }

    @Override
    public List<ProductDto> findAll() {
        return productsData.values().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findByCategoryId(String categoryId) {
        return productsData.values().stream()
                .filter(data -> categoryId.equals(data.get("category_id")))
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findBySellerId(Long sellerId) {
        return productsData.values().stream()
                .filter(data -> sellerId.equals(((Number) data.get("seller_id")).longValue()))
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ProductDto mapToProductDto(Map<String, Object> data) {
        return ProductDto.builder()
                .id(getString(data, "id"))
                .siteId(getString(data, "site_id"))
                .title(getString(data, "title"))
                .subtitle(getString(data, "subtitle"))
                .sellerId(data.get("seller_id") != null ? ((Number) data.get("seller_id")).longValue() : null)
                .categoryId(getString(data, "category_id"))
                .officialStoreId(getString(data, "official_store_id"))
                .price(data.get("price") != null ? ((Number) data.get("price")).doubleValue() : null)
                .basePrice(data.get("base_price") != null ? ((Number) data.get("base_price")).doubleValue() : null)
                .originalPrice(data.get("original_price") != null ? ((Number) data.get("original_price")).doubleValue() : null)
                .currencyId(getString(data, "currency_id"))
                .initialQuantity(data.get("initial_quantity") != null ? ((Number) data.get("initial_quantity")).intValue() : null)
                .availableQuantity(data.get("available_quantity") != null ? ((Number) data.get("available_quantity")).intValue() : null)
                .soldQuantity(data.get("sold_quantity") != null ? ((Number) data.get("sold_quantity")).intValue() : null)
                .condition(getString(data, "condition"))
                .permalink(getString(data, "permalink"))
                .thumbnail(getString(data, "thumbnail"))
                .secureThumbnail(getString(data, "secure_thumbnail"))
                .pictures(mapPictures((List<Map<String, Object>>) data.get("pictures")))
                .videoId(getString(data, "video_id"))
                .descriptions(mapDescriptions((List<Map<String, Object>>) data.get("descriptions")))
                .acceptsMercadopago((Boolean) data.get("accepts_mercadopago"))
                .nonMercadoPagoPaymentMethods((List<String>) data.get("non_mercado_pago_payment_methods"))
                .shipping(mapShipping((Map<String, Object>) data.get("shipping")))
                .sellerAddress(mapSellerAddress((Map<String, Object>) data.get("seller_address")))
                .attributes(mapAttributes((List<Map<String, Object>>) data.get("attributes")))
                .warnings((List<String>) data.get("warnings"))
                .listingSource(getString(data, "listing_source"))
                .variations((List<ProductDto.VariationDto>) data.get("variations"))
                .status(getString(data, "status"))
                .warranty(getString(data, "warranty"))
                .catalogProductId(getString(data, "catalog_product_id"))
                .domainId(getString(data, "domain_id"))
                .sellerContact(getString(data, "seller_contact"))
                .location(ProductDto.LocationDto.builder().build())
                .geolocation(mapGeolocation((Map<String, Object>) data.get("geolocation")))
                .coverageAreas((List<String>) data.get("coverage_areas"))
                .sellerCustomField(getString(data, "seller_custom_field"))
                .parentItemId(getString(data, "parent_item_id"))
                .differentialPricing(getString(data, "differential_pricing"))
                .dealIds((List<String>) data.get("deal_ids"))
                .automaticRelist((Boolean) data.get("automatic_relist"))
                .dateCreated(parseDateTime(getString(data, "date_created")))
                .lastUpdated(parseDateTime(getString(data, "last_updated")))
                .build();
    }

    private List<ProductDto.PictureDto> mapPictures(List<Map<String, Object>> pictures) {
        if (pictures == null) return List.of();
        return pictures.stream()
                .map(pic -> ProductDto.PictureDto.builder()
                        .id((String) pic.get("id"))
                        .url((String) pic.get("url"))
                        .secureUrl((String) pic.get("secure_url"))
                        .size((String) pic.get("size"))
                        .maxSize((String) pic.get("max_size"))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProductDto.DescriptionDto> mapDescriptions(List<Map<String, Object>> descriptions) {
        if (descriptions == null) return List.of();
        return descriptions.stream()
                .map(desc -> ProductDto.DescriptionDto.builder()
                        .id((String) desc.get("id"))
                        .text((String) desc.get("text"))
                        .build())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ProductDto.ShippingDto mapShipping(Map<String, Object> shipping) {
        if (shipping == null) return null;
        return ProductDto.ShippingDto.builder()
                .mode((String) shipping.get("mode"))
                .methods(mapShippingMethods((List<Map<String, Object>>) shipping.get("methods")))
                .tags((List<String>) shipping.get("tags"))
                .dimensions((String) shipping.get("dimensions"))
                .localPickUp((Boolean) shipping.get("local_pick_up"))
                .freeShipping((Boolean) shipping.get("free_shipping"))
                .logisticType((String) shipping.get("logistic_type"))
                .storePickUp((Boolean) shipping.get("store_pick_up"))
                .build();
    }

    private List<ProductDto.ShippingMethodDto> mapShippingMethods(List<Map<String, Object>> methods) {
        if (methods == null) return List.of();
        return methods.stream()
                .map(method -> ProductDto.ShippingMethodDto.builder()
                        .id((String) method.get("id"))
                        .name((String) method.get("name"))
                        .build())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ProductDto.SellerAddressDto mapSellerAddress(Map<String, Object> sellerAddress) {
        if (sellerAddress == null) return null;
        return ProductDto.SellerAddressDto.builder()
                .city(mapCity((Map<String, Object>) sellerAddress.get("city")))
                .state(mapState((Map<String, Object>) sellerAddress.get("state")))
                .country(mapCountry((Map<String, Object>) sellerAddress.get("country")))
                .searchLocation(mapSearchLocation((Map<String, Object>) sellerAddress.get("search_location")))
                .id(sellerAddress.get("id") != null ? ((Number) sellerAddress.get("id")).longValue() : null)
                .build();
    }

    private ProductDto.CityDto mapCity(Map<String, Object> city) {
        if (city == null) return null;
        return ProductDto.CityDto.builder()
                .id((String) city.get("id"))
                .name((String) city.get("name"))
                .build();
    }

    private ProductDto.StateDto mapState(Map<String, Object> state) {
        if (state == null) return null;
        return ProductDto.StateDto.builder()
                .id((String) state.get("id"))
                .name((String) state.get("name"))
                .build();
    }

    private ProductDto.CountryDto mapCountry(Map<String, Object> country) {
        if (country == null) return null;
        return ProductDto.CountryDto.builder()
                .id((String) country.get("id"))
                .name((String) country.get("name"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private ProductDto.SearchLocationDto mapSearchLocation(Map<String, Object> searchLocation) {
        if (searchLocation == null) return null;
        return ProductDto.SearchLocationDto.builder()
                .neighborhood(mapNeighborhood((Map<String, Object>) searchLocation.get("neighborhood")))
                .city(mapCity((Map<String, Object>) searchLocation.get("city")))
                .state(mapState((Map<String, Object>) searchLocation.get("state")))
                .build();
    }

    private ProductDto.NeighborhoodDto mapNeighborhood(Map<String, Object> neighborhood) {
        if (neighborhood == null) return null;
        return ProductDto.NeighborhoodDto.builder()
                .id((String) neighborhood.get("id"))
                .name((String) neighborhood.get("name"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<ProductDto.AttributeDto> mapAttributes(List<Map<String, Object>> attributes) {
        if (attributes == null) return List.of();
        return attributes.stream()
                .map(attr -> ProductDto.AttributeDto.builder()
                        .id((String) attr.get("id"))
                        .name((String) attr.get("name"))
                        .valueId((String) attr.get("value_id"))
                        .valueName((String) attr.get("value_name"))
                        .valueStruct((String) attr.get("value_struct"))
                        .values(mapAttributeValues((List<Map<String, Object>>) attr.get("values")))
                        .attributeGroupId((String) attr.get("attribute_group_id"))
                        .attributeGroupName((String) attr.get("attribute_group_name"))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProductDto.AttributeValueDto> mapAttributeValues(List<Map<String, Object>> values) {
        if (values == null) return List.of();
        return values.stream()
                .map(value -> ProductDto.AttributeValueDto.builder()
                        .id((String) value.get("id"))
                        .name((String) value.get("name"))
                        .struct((String) value.get("struct"))
                        .build())
                .collect(Collectors.toList());
    }

    private ProductDto.GeolocationDto mapGeolocation(Map<String, Object> geolocation) {
        if (geolocation == null) return null;
        return ProductDto.GeolocationDto.builder()
                .latitude(geolocation.get("latitude") != null ? ((Number) geolocation.get("latitude")).doubleValue() : null)
                .longitude(geolocation.get("longitude") != null ? ((Number) geolocation.get("longitude")).doubleValue() : null)
                .build();
    }

    private LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null) return null;
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
}
