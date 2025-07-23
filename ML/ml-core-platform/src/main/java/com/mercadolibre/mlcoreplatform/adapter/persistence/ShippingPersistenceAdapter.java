package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ShippingDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.ShippingPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class ShippingPersistenceAdapter implements ShippingPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, List<Map<String, Object>>> shippingData;

    public ShippingPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("shipping.json");
        this.shippingData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, List<Map<String, Object>>>>() {}
        );
    }

    @Override
    public List<ShippingDto> findByItemId(String itemId) {
        List<Map<String, Object>> itemShipping = shippingData.get(itemId);
        if (itemShipping == null) {
            return List.of();
        }
        
        return itemShipping.stream()
                .map(this::mapToShippingDto)
                .toList();
    }

    @Override
    public List<ShippingDto> findAll() {
        return shippingData.values().stream()
                .flatMap(List::stream)
                .map(this::mapToShippingDto)
                .toList();
    }

    @Override
    public List<ShippingDto> findByDeliveryType(String deliveryType) {
        return shippingData.values().stream()
                .flatMap(List::stream)
                .filter(shipping -> deliveryType.equals(shipping.get("delivery_type")))
                .map(this::mapToShippingDto)
                .toList();
    }

    @Override
    public List<ShippingDto> findFreeShippingOptions() {
        return shippingData.values().stream()
                .flatMap(List::stream)
                .filter(shipping -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> freeShipping = (Map<String, Object>) shipping.get("free_shipping");
                    return freeShipping != null && Boolean.TRUE.equals(freeShipping.get("flag"));
                })
                .map(this::mapToShippingDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private ShippingDto mapToShippingDto(Map<String, Object> data) {
        return ShippingDto.builder()
                .id((String) data.get("id"))
                .name((String) data.get("name"))
                .displayName((String) data.get("display_name"))
                .currencyId((String) data.get("currency_id"))
                .listCost(data.get("list_cost") != null ? ((Number) data.get("list_cost")).doubleValue() : null)
                .cost(data.get("cost") != null ? ((Number) data.get("cost")).doubleValue() : null)
                .freeShipping(mapFreeShipping((Map<String, Object>) data.get("free_shipping")))
                .tracksShipmentsStatus((String) data.get("tracks_shipments_status"))
                .estimatedScheduleLimit(mapEstimatedScheduleLimit((Map<String, Object>) data.get("estimated_schedule_limit")))
                .estimatedDelivery(mapEstimatedDelivery((Map<String, Object>) data.get("estimated_delivery")))
                .speedRanking(data.get("speed_ranking") != null ? ((Number) data.get("speed_ranking")).intValue() : null)
                .deliveryType((String) data.get("delivery_type"))
                .shippingMethodId(data.get("shipping_method_id") != null ? ((Number) data.get("shipping_method_id")).longValue() : null)
                .coverage(mapCoverage((Map<String, Object>) data.get("coverage")))
                .company(mapCompany((Map<String, Object>) data.get("company")))
                .handlingTime(mapHandlingTime((Map<String, Object>) data.get("handling_time")))
                .pickupPoints(mapPickupPoints((List<Map<String, Object>>) data.get("pickup_points")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private ShippingDto.FreeShippingDto mapFreeShipping(Map<String, Object> freeShipping) {
        if (freeShipping == null) return null;
        return ShippingDto.FreeShippingDto.builder()
                .flag((Boolean) freeShipping.get("flag"))
                .rule(mapRule((Map<String, Object>) freeShipping.get("rule")))
                .build();
    }

    private ShippingDto.RuleDto mapRule(Map<String, Object> rule) {
        if (rule == null) return null;
        return ShippingDto.RuleDto.builder()
                .freeMode((String) rule.get("free_mode"))
                .value(rule.get("value") != null ? ((Number) rule.get("value")).longValue() : null)
                .build();
    }

    private ShippingDto.EstimatedScheduleLimitDto mapEstimatedScheduleLimit(Map<String, Object> estimatedScheduleLimit) {
        if (estimatedScheduleLimit == null) return null;
        return ShippingDto.EstimatedScheduleLimitDto.builder()
                .date(parseDateTime((String) estimatedScheduleLimit.get("date")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private ShippingDto.EstimatedDeliveryDto mapEstimatedDelivery(Map<String, Object> estimatedDelivery) {
        if (estimatedDelivery == null) return null;
        return ShippingDto.EstimatedDeliveryDto.builder()
                .date(parseDateTime((String) estimatedDelivery.get("date")))
                .unit((String) estimatedDelivery.get("unit"))
                .offset(mapOffset((Map<String, Object>) estimatedDelivery.get("offset")))
                .build();
    }

    private ShippingDto.OffsetDto mapOffset(Map<String, Object> offset) {
        if (offset == null) return null;
        return ShippingDto.OffsetDto.builder()
                .date(offset.get("date") != null ? ((Number) offset.get("date")).intValue() : null)
                .shipping(offset.get("shipping") != null ? ((Number) offset.get("shipping")).intValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private ShippingDto.CoverageDto mapCoverage(Map<String, Object> coverage) {
        if (coverage == null) return null;
        return ShippingDto.CoverageDto.builder()
                .allCountry((Boolean) coverage.get("all_country"))
                .specificPlaces(mapSpecificPlaces((Map<String, Object>) coverage.get("specific_places")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private ShippingDto.SpecificPlacesDto mapSpecificPlaces(Map<String, Object> specificPlaces) {
        if (specificPlaces == null) return null;
        return ShippingDto.SpecificPlacesDto.builder()
                .included((List<String>) specificPlaces.get("included"))
                .excluded((List<String>) specificPlaces.get("excluded"))
                .build();
    }

    private ShippingDto.CompanyDto mapCompany(Map<String, Object> company) {
        if (company == null) return null;
        return ShippingDto.CompanyDto.builder()
                .id((String) company.get("id"))
                .name((String) company.get("name"))
                .logo((String) company.get("logo"))
                .build();
    }

    private ShippingDto.HandlingTimeDto mapHandlingTime(Map<String, Object> handlingTime) {
        if (handlingTime == null) return null;
        return ShippingDto.HandlingTimeDto.builder()
                .excludeWeekends((Boolean) handlingTime.get("exclude_weekends"))
                .unit((String) handlingTime.get("unit"))
                .value(handlingTime.get("value") != null ? ((Number) handlingTime.get("value")).intValue() : null)
                .build();
    }

    private List<ShippingDto.PickupPointDto> mapPickupPoints(List<Map<String, Object>> pickupPoints) {
        if (pickupPoints == null) return List.of();
        return pickupPoints.stream()
                .map(point -> ShippingDto.PickupPointDto.builder()
                        .id((String) point.get("id"))
                        .name((String) point.get("name"))
                        .address((String) point.get("address"))
                        .city((String) point.get("city"))
                        .state((String) point.get("state"))
                        .zipCode((String) point.get("zip_code"))
                        .phone((String) point.get("phone"))
                        .hours((String) point.get("hours"))
                        .latitude(point.get("latitude") != null ? ((Number) point.get("latitude")).doubleValue() : null)
                        .longitude(point.get("longitude") != null ? ((Number) point.get("longitude")).doubleValue() : null)
                        .build())
                .toList();
    }

    private LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null) return null;
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
}
