package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PurchaseDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.PurchasePersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PurchasePersistenceAdapter implements PurchasePersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Object>> purchasesData;

    public PurchasePersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("purchases.json");
        this.purchasesData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, Map<String, Object>>>() {}
        );
    }

    @Override
    public Optional<PurchaseDto> findById(String orderId) {
        Map<String, Object> purchaseData = purchasesData.get(orderId);
        if (purchaseData == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapToPurchaseDto(purchaseData));
    }

    @Override
    public List<PurchaseDto> findAll() {
        return purchasesData.values().stream()
                .map(this::mapToPurchaseDto)
                .toList();
    }

    @Override
    public List<PurchaseDto> findByStatus(String status) {
        return purchasesData.values().stream()
                .filter(data -> status.equals(data.get("status")))
                .map(this::mapToPurchaseDto)
                .toList();
    }

    @Override
    public List<PurchaseDto> findByBuyerId(Long buyerId) {
        return purchasesData.values().stream()
                .filter(data -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> buyer = (Map<String, Object>) data.get("buyer");
                    return buyer != null && buyerId.equals(((Number) buyer.get("id")).longValue());
                })
                .map(this::mapToPurchaseDto)
                .toList();
    }

    @Override
    public List<PurchaseDto> findBySellerId(Long sellerId) {
        return purchasesData.values().stream()
                .filter(data -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> seller = (Map<String, Object>) data.get("seller");
                    return seller != null && sellerId.equals(((Number) seller.get("id")).longValue());
                })
                .map(this::mapToPurchaseDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto mapToPurchaseDto(Map<String, Object> data) {
        return PurchaseDto.builder()
                .id((String) data.get("id"))
                .dateCreated(parseDateTime((String) data.get("date_created")))
                .dateClosed(parseDateTime((String) data.get("date_closed")))
                .lastUpdated(parseDateTime((String) data.get("last_updated")))
                .status((String) data.get("status"))
                .statusDetail(mapStatusDetail((Map<String, Object>) data.get("status_detail")))
                .currencyId((String) data.get("currency_id"))
                .orderItems(mapOrderItems((List<Map<String, Object>>) data.get("order_items")))
                .totalAmount(data.get("total_amount") != null ? ((Number) data.get("total_amount")).doubleValue() : null)
                .paidAmount(data.get("paid_amount") != null ? ((Number) data.get("paid_amount")).doubleValue() : null)
                .coupon(mapCoupon((Map<String, Object>) data.get("coupon")))
                .expirationDate(parseDateTime((String) data.get("expiration_date")))
                .shipping(mapShippingInfo((Map<String, Object>) data.get("shipping")))
                .payments(mapPaymentInfos((List<Map<String, Object>>) data.get("payments")))
                .buyer(mapBuyer((Map<String, Object>) data.get("buyer")))
                .seller(mapPurchaseSeller((Map<String, Object>) data.get("seller")))
                .tags((List<String>) data.get("tags"))
                .feedback(mapFeedback((Map<String, Object>) data.get("feedback")))
                .context(mapContext((Map<String, Object>) data.get("context")))
                .build();
    }

    private PurchaseDto.StatusDetailDto mapStatusDetail(Map<String, Object> statusDetail) {
        if (statusDetail == null) return null;
        return PurchaseDto.StatusDetailDto.builder()
                .code((String) statusDetail.get("code"))
                .description((String) statusDetail.get("description"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<PurchaseDto.OrderItemDto> mapOrderItems(List<Map<String, Object>> orderItems) {
        if (orderItems == null) return List.of();
        return orderItems.stream()
                .map(item -> PurchaseDto.OrderItemDto.builder()
                        .item(mapItem((Map<String, Object>) item.get("item")))
                        .quantity(item.get("quantity") != null ? ((Number) item.get("quantity")).intValue() : null)
                        .unitPrice(item.get("unit_price") != null ? ((Number) item.get("unit_price")).doubleValue() : null)
                        .fullUnitPrice(item.get("full_unit_price") != null ? ((Number) item.get("full_unit_price")).doubleValue() : null)
                        .currencyId((String) item.get("currency_id"))
                        .manufacturingEndingDate(parseDateTime((String) item.get("manufacturing_ending_date")))
                        .saleFee(item.get("sale_fee") != null ? ((Number) item.get("sale_fee")).doubleValue() : null)
                        .listingTypeId((String) item.get("listing_type_id"))
                        .build())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.ItemDto mapItem(Map<String, Object> item) {
        if (item == null) return null;
        return PurchaseDto.ItemDto.builder()
                .id((String) item.get("id"))
                .title((String) item.get("title"))
                .categoryId((String) item.get("category_id"))
                .variationId((String) item.get("variation_id"))
                .sellerCustomField((String) item.get("seller_custom_field"))
                .variationAttributes((List<String>) item.get("variation_attributes"))
                .warranty((String) item.get("warranty"))
                .condition((String) item.get("condition"))
                .sellerSku((String) item.get("seller_sku"))
                .globalPrice((String) item.get("global_price"))
                .netWeight((String) item.get("net_weight"))
                .build();
    }

    private PurchaseDto.CouponDto mapCoupon(Map<String, Object> coupon) {
        if (coupon == null) return null;
        return PurchaseDto.CouponDto.builder()
                .id((String) coupon.get("id"))
                .amount(coupon.get("amount") != null ? ((Number) coupon.get("amount")).doubleValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.ShippingInfoDto mapShippingInfo(Map<String, Object> shipping) {
        if (shipping == null) return null;
        return PurchaseDto.ShippingInfoDto.builder()
                .id(shipping.get("id") != null ? ((Number) shipping.get("id")).longValue() : null)
                .shipmentType((String) shipping.get("shipment_type"))
                .status((String) shipping.get("status"))
                .dateCreated(parseDateTime((String) shipping.get("date_created")))
                .lastUpdated(parseDateTime((String) shipping.get("last_updated")))
                .trackingNumber((String) shipping.get("tracking_number"))
                .trackingMethod((String) shipping.get("tracking_method"))
                .serviceId(shipping.get("service_id") != null ? ((Number) shipping.get("service_id")).intValue() : null)
                .dateFirstPrinted(parseDateTime((String) shipping.get("date_first_printed")))
                .carrierInfo(mapCarrierInfo((Map<String, Object>) shipping.get("carrier_info")))
                .shippingItems(mapShippingItems((List<Map<String, Object>>) shipping.get("shipping_items")))
                .shippingOption(mapShippingOption((Map<String, Object>) shipping.get("shipping_option")))
                .receiverAddress(mapReceiverAddress((Map<String, Object>) shipping.get("receiver_address")))
                .build();
    }

    private PurchaseDto.CarrierInfoDto mapCarrierInfo(Map<String, Object> carrierInfo) {
        if (carrierInfo == null) return null;
        return PurchaseDto.CarrierInfoDto.builder()
                .name((String) carrierInfo.get("name"))
                .logo((String) carrierInfo.get("logo"))
                .build();
    }

    private List<PurchaseDto.ShippingItemDto> mapShippingItems(List<Map<String, Object>> shippingItems) {
        if (shippingItems == null) return List.of();
        return shippingItems.stream()
                .map(shippingItem -> PurchaseDto.ShippingItemDto.builder()
                        .id((String) shippingItem.get("id"))
                        .description((String) shippingItem.get("description"))
                        .quantity(shippingItem.get("quantity") != null ? ((Number) shippingItem.get("quantity")).intValue() : null)
                        .dimensions((String) shippingItem.get("dimensions"))
                        .build())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.ShippingOptionDto mapShippingOption(Map<String, Object> shippingOption) {
        if (shippingOption == null) return null;
        return PurchaseDto.ShippingOptionDto.builder()
                .id(shippingOption.get("id") != null ? ((Number) shippingOption.get("id")).longValue() : null)
                .name((String) shippingOption.get("name"))
                .cost(shippingOption.get("cost") != null ? ((Number) shippingOption.get("cost")).doubleValue() : null)
                .currencyId((String) shippingOption.get("currency_id"))
                .estimatedDelivery(mapEstimatedDelivery((Map<String, Object>) shippingOption.get("estimated_delivery")))
                .build();
    }

    private PurchaseDto.EstimatedDeliveryDto mapEstimatedDelivery(Map<String, Object> estimatedDelivery) {
        if (estimatedDelivery == null) return null;
        return PurchaseDto.EstimatedDeliveryDto.builder()
                .date(parseDateTime((String) estimatedDelivery.get("date")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.ReceiverAddressDto mapReceiverAddress(Map<String, Object> receiverAddress) {
        if (receiverAddress == null) return null;
        return PurchaseDto.ReceiverAddressDto.builder()
                .id(receiverAddress.get("id") != null ? ((Number) receiverAddress.get("id")).longValue() : null)
                .addressLine((String) receiverAddress.get("address_line"))
                .streetName((String) receiverAddress.get("street_name"))
                .streetNumber((String) receiverAddress.get("street_number"))
                .comment((String) receiverAddress.get("comment"))
                .zipCode((String) receiverAddress.get("zip_code"))
                .city(mapCity((Map<String, Object>) receiverAddress.get("city")))
                .state(mapState((Map<String, Object>) receiverAddress.get("state")))
                .country(mapCountry((Map<String, Object>) receiverAddress.get("country")))
                .latitude(receiverAddress.get("latitude") != null ? ((Number) receiverAddress.get("latitude")).doubleValue() : null)
                .longitude(receiverAddress.get("longitude") != null ? ((Number) receiverAddress.get("longitude")).doubleValue() : null)
                .receiverName((String) receiverAddress.get("receiver_name"))
                .receiverPhone((String) receiverAddress.get("receiver_phone"))
                .build();
    }

    private PurchaseDto.CityDto mapCity(Map<String, Object> city) {
        if (city == null) return null;
        return PurchaseDto.CityDto.builder()
                .id((String) city.get("id"))
                .name((String) city.get("name"))
                .build();
    }

    private PurchaseDto.StateDto mapState(Map<String, Object> state) {
        if (state == null) return null;
        return PurchaseDto.StateDto.builder()
                .id((String) state.get("id"))
                .name((String) state.get("name"))
                .build();
    }

    private PurchaseDto.CountryDto mapCountry(Map<String, Object> country) {
        if (country == null) return null;
        return PurchaseDto.CountryDto.builder()
                .id((String) country.get("id"))
                .name((String) country.get("name"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<PurchaseDto.PaymentInfoDto> mapPaymentInfos(List<Map<String, Object>> payments) {
        if (payments == null) return List.of();
        return payments.stream()
                .map(payment -> PurchaseDto.PaymentInfoDto.builder()
                        .id(payment.get("id") != null ? ((Number) payment.get("id")).longValue() : null)
                        .orderId((String) payment.get("order_id"))
                        .payerId(payment.get("payer_id") != null ? ((Number) payment.get("payer_id")).longValue() : null)
                        .collector(mapCollector((Map<String, Object>) payment.get("collector")))
                        .cardId((String) payment.get("card_id"))
                        .siteId((String) payment.get("site_id"))
                        .reason((String) payment.get("reason"))
                        .paymentMethodId((String) payment.get("payment_method_id"))
                        .paymentType((String) payment.get("payment_type"))
                        .status((String) payment.get("status"))
                        .statusDetail((String) payment.get("status_detail"))
                        .currencyId((String) payment.get("currency_id"))
                        .description((String) payment.get("description"))
                        .installments(payment.get("installments") != null ? ((Number) payment.get("installments")).intValue() : null)
                        .issuerId((String) payment.get("issuer_id"))
                        .totalPaidAmount(payment.get("total_paid_amount") != null ? ((Number) payment.get("total_paid_amount")).doubleValue() : null)
                        .shippingCost(payment.get("shipping_cost") != null ? ((Number) payment.get("shipping_cost")).doubleValue() : null)
                        .couponAmount(payment.get("coupon_amount") != null ? ((Number) payment.get("coupon_amount")).doubleValue() : null)
                        .dateCreated(parseDateTime((String) payment.get("date_created")))
                        .dateLastUpdated(parseDateTime((String) payment.get("date_last_updated")))
                        .dateApproved(parseDateTime((String) payment.get("date_approved")))
                        .authorizationCode((String) payment.get("authorization_code"))
                        .captured((Boolean) payment.get("captured"))
                        .liveMode((Boolean) payment.get("live_mode"))
                        .operationType((String) payment.get("operation_type"))
                        .build())
                .toList();
    }

    private PurchaseDto.CollectorDto mapCollector(Map<String, Object> collector) {
        if (collector == null) return null;
        return PurchaseDto.CollectorDto.builder()
                .id(collector.get("id") != null ? ((Number) collector.get("id")).longValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.BuyerDto mapBuyer(Map<String, Object> buyer) {
        if (buyer == null) return null;
        return PurchaseDto.BuyerDto.builder()
                .id(buyer.get("id") != null ? ((Number) buyer.get("id")).longValue() : null)
                .nickname((String) buyer.get("nickname"))
                .email((String) buyer.get("email"))
                .phone(mapPhone((Map<String, Object>) buyer.get("phone")))
                .firstName((String) buyer.get("first_name"))
                .lastName((String) buyer.get("last_name"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.PurchaseSellerDto mapPurchaseSeller(Map<String, Object> seller) {
        if (seller == null) return null;
        return PurchaseDto.PurchaseSellerDto.builder()
                .id(seller.get("id") != null ? ((Number) seller.get("id")).longValue() : null)
                .nickname((String) seller.get("nickname"))
                .email((String) seller.get("email"))
                .phone(mapPhone((Map<String, Object>) seller.get("phone")))
                .firstName((String) seller.get("first_name"))
                .lastName((String) seller.get("last_name"))
                .build();
    }

    private PurchaseDto.PhoneDto mapPhone(Map<String, Object> phone) {
        if (phone == null) return null;
        return PurchaseDto.PhoneDto.builder()
                .areaCode((String) phone.get("area_code"))
                .number((String) phone.get("number"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.FeedbackDto mapFeedback(Map<String, Object> feedback) {
        if (feedback == null) return null;
        return PurchaseDto.FeedbackDto.builder()
                .purchase(mapPurchaseFeedback((Map<String, Object>) feedback.get("purchase")))
                .sale(mapSaleFeedback((Map<String, Object>) feedback.get("sale")))
                .build();
    }

    private PurchaseDto.PurchaseFeedbackDto mapPurchaseFeedback(Map<String, Object> purchaseFeedback) {
        if (purchaseFeedback == null) return null;
        return PurchaseDto.PurchaseFeedbackDto.builder()
                .rating(purchaseFeedback.get("rating") != null ? ((Number) purchaseFeedback.get("rating")).intValue() : null)
                .message((String) purchaseFeedback.get("message"))
                .build();
    }

    private PurchaseDto.SaleFeedbackDto mapSaleFeedback(Map<String, Object> saleFeedback) {
        if (saleFeedback == null) return null;
        return PurchaseDto.SaleFeedbackDto.builder()
                .rating(saleFeedback.get("rating") != null ? ((Number) saleFeedback.get("rating")).intValue() : null)
                .message((String) saleFeedback.get("message"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private PurchaseDto.ContextDto mapContext(Map<String, Object> context) {
        if (context == null) return null;
        return PurchaseDto.ContextDto.builder()
                .channel((String) context.get("channel"))
                .site((String) context.get("site"))
                .flows((List<String>) context.get("flows"))
                .application((String) context.get("application"))
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
}
