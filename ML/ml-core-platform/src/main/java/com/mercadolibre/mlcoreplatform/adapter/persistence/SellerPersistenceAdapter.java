package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.SellerDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.SellerPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SellerPersistenceAdapter implements SellerPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Object>> sellersData;

    public SellerPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("sellers.json");
        this.sellersData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, Map<String, Object>>>() {}
        );
    }

    @Override
    public Optional<SellerDto> findById(Long sellerId) {
        if (sellerId == null) {
            return Optional.empty();
        }
        
        Map<String, Object> sellerData = sellersData.get(sellerId.toString());
        if (sellerData == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapToSellerDto(sellerData));
    }

    @Override
    public List<SellerDto> findAll() {
        return sellersData.values().stream()
                .map(this::mapToSellerDto)
                .toList();
    }

    @Override
    public List<SellerDto> findByNickname(String nickname) {
        return sellersData.values().stream()
                .filter(data -> nickname.equals(data.get("nickname")))
                .map(this::mapToSellerDto)
                .toList();
    }

    @Override
    public List<SellerDto> findByCountryId(String countryId) {
        return sellersData.values().stream()
                .filter(data -> countryId.equals(data.get("country_id")))
                .map(this::mapToSellerDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private SellerDto mapToSellerDto(Map<String, Object> data) {
        return SellerDto.builder()
                .id(data.get("id") != null ? ((Number) data.get("id")).longValue() : null)
                .nickname((String) data.get("nickname"))
                .registrationDate(parseDateTime((String) data.get("registration_date")))
                .firstName((String) data.get("first_name"))
                .lastName((String) data.get("last_name"))
                .countryId((String) data.get("country_id"))
                .email((String) data.get("email"))
                .identification(mapIdentification((Map<String, Object>) data.get("identification")))
                .address(mapAddress((Map<String, Object>) data.get("address")))
                .phone(mapPhone((Map<String, Object>) data.get("phone")))
                .alternativePhone(mapPhone((Map<String, Object>) data.get("alternative_phone")))
                .userType((String) data.get("user_type"))
                .tags((List<String>) data.get("tags"))
                .logo((String) data.get("logo"))
                .points(data.get("points") != null ? ((Number) data.get("points")).intValue() : null)
                .siteId((String) data.get("site_id"))
                .permalink((String) data.get("permalink"))
                .sellerReputation(mapSellerReputation((Map<String, Object>) data.get("seller_reputation")))
                .buyerReputation(mapBuyerReputation((Map<String, Object>) data.get("buyer_reputation")))
                .status(mapStatus((Map<String, Object>) data.get("status")))
                .build();
    }

    private SellerDto.IdentificationDto mapIdentification(Map<String, Object> identification) {
        if (identification == null) return null;
        return SellerDto.IdentificationDto.builder()
                .type((String) identification.get("type"))
                .number((String) identification.get("number"))
                .build();
    }

    private SellerDto.AddressDto mapAddress(Map<String, Object> address) {
        if (address == null) return null;
        return SellerDto.AddressDto.builder()
                .city((String) address.get("city"))
                .state((String) address.get("state"))
                .zipCode((String) address.get("zip_code"))
                .build();
    }

    private SellerDto.PhoneDto mapPhone(Map<String, Object> phone) {
        if (phone == null) return null;
        return SellerDto.PhoneDto.builder()
                .areaCode((String) phone.get("area_code"))
                .number((String) phone.get("number"))
                .extension((String) phone.get("extension"))
                .verified((Boolean) phone.get("verified"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.SellerReputationDto mapSellerReputation(Map<String, Object> reputation) {
        if (reputation == null) return null;
        return SellerDto.SellerReputationDto.builder()
                .powerSellerStatus((String) reputation.get("power_seller_status"))
                .levelId((String) reputation.get("level_id"))
                .metrics(mapMetrics((Map<String, Object>) reputation.get("metrics")))
                .transactions(mapTransactions((Map<String, Object>) reputation.get("transactions")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.MetricsDto mapMetrics(Map<String, Object> metrics) {
        if (metrics == null) return null;
        return SellerDto.MetricsDto.builder()
                .cancellations(mapCancellations((Map<String, Object>) metrics.get("cancellations")))
                .claims(mapClaims((Map<String, Object>) metrics.get("claims")))
                .delayedHandlingTime(mapDelayedHandlingTime((Map<String, Object>) metrics.get("delayed_handling_time")))
                .sales(mapSales((Map<String, Object>) metrics.get("sales")))
                .build();
    }

    private SellerDto.CancellationsDto mapCancellations(Map<String, Object> cancellations) {
        if (cancellations == null) return null;
        return SellerDto.CancellationsDto.builder()
                .period((String) cancellations.get("period"))
                .rate(cancellations.get("rate") != null ? ((Number) cancellations.get("rate")).doubleValue() : null)
                .value(cancellations.get("value") != null ? ((Number) cancellations.get("value")).intValue() : null)
                .build();
    }

    private SellerDto.ClaimsDto mapClaims(Map<String, Object> claims) {
        if (claims == null) return null;
        return SellerDto.ClaimsDto.builder()
                .period((String) claims.get("period"))
                .rate(claims.get("rate") != null ? ((Number) claims.get("rate")).doubleValue() : null)
                .value(claims.get("value") != null ? ((Number) claims.get("value")).intValue() : null)
                .build();
    }

    private SellerDto.DelayedHandlingTimeDto mapDelayedHandlingTime(Map<String, Object> delayedHandlingTime) {
        if (delayedHandlingTime == null) return null;
        return SellerDto.DelayedHandlingTimeDto.builder()
                .period((String) delayedHandlingTime.get("period"))
                .rate(delayedHandlingTime.get("rate") != null ? ((Number) delayedHandlingTime.get("rate")).doubleValue() : null)
                .value(delayedHandlingTime.get("value") != null ? ((Number) delayedHandlingTime.get("value")).intValue() : null)
                .build();
    }

    private SellerDto.SalesDto mapSales(Map<String, Object> sales) {
        if (sales == null) return null;
        return SellerDto.SalesDto.builder()
                .period((String) sales.get("period"))
                .completed(sales.get("completed") != null ? ((Number) sales.get("completed")).intValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.TransactionsDto mapTransactions(Map<String, Object> transactions) {
        if (transactions == null) return null;
        return SellerDto.TransactionsDto.builder()
                .canceled(transactions.get("canceled") != null ? ((Number) transactions.get("canceled")).intValue() : null)
                .completed(transactions.get("completed") != null ? ((Number) transactions.get("completed")).intValue() : null)
                .period((String) transactions.get("period"))
                .ratings(mapRatings((Map<String, Object>) transactions.get("ratings")))
                .total(transactions.get("total") != null ? ((Number) transactions.get("total")).intValue() : null)
                .build();
    }

    private SellerDto.RatingsDto mapRatings(Map<String, Object> ratings) {
        if (ratings == null) return null;
        return SellerDto.RatingsDto.builder()
                .negative(ratings.get("negative") != null ? ((Number) ratings.get("negative")).intValue() : null)
                .neutral(ratings.get("neutral") != null ? ((Number) ratings.get("neutral")).intValue() : null)
                .positive(ratings.get("positive") != null ? ((Number) ratings.get("positive")).intValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.BuyerReputationDto mapBuyerReputation(Map<String, Object> reputation) {
        if (reputation == null) return null;
        return SellerDto.BuyerReputationDto.builder()
                .canceledTransactions(reputation.get("canceled_transactions") != null ? 
                    ((Number) reputation.get("canceled_transactions")).intValue() : null)
                .tags((List<String>) reputation.get("tags"))
                .transactions(mapBuyerTransactions((Map<String, Object>) reputation.get("transactions")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.BuyerTransactionsDto mapBuyerTransactions(Map<String, Object> transactions) {
        if (transactions == null) return null;
        return SellerDto.BuyerTransactionsDto.builder()
                .canceled(mapCanceled((Map<String, Object>) transactions.get("canceled")))
                .completed(transactions.get("completed") != null ? ((Number) transactions.get("completed")).intValue() : null)
                .notYetRated(mapNotYetRated((Map<String, Object>) transactions.get("not_yet_rated")))
                .period((String) transactions.get("period"))
                .total(transactions.get("total") != null ? ((Number) transactions.get("total")).intValue() : null)
                .unrated(mapUnrated((Map<String, Object>) transactions.get("unrated")))
                .build();
    }

    private SellerDto.CanceledDto mapCanceled(Map<String, Object> canceled) {
        if (canceled == null) return null;
        return SellerDto.CanceledDto.builder()
                .paid(canceled.get("paid") != null ? ((Number) canceled.get("paid")).intValue() : null)
                .total(canceled.get("total") != null ? ((Number) canceled.get("total")).intValue() : null)
                .build();
    }

    private SellerDto.NotYetRatedDto mapNotYetRated(Map<String, Object> notYetRated) {
        if (notYetRated == null) return null;
        return SellerDto.NotYetRatedDto.builder()
                .paid(notYetRated.get("paid") != null ? ((Number) notYetRated.get("paid")).intValue() : null)
                .total(notYetRated.get("total") != null ? ((Number) notYetRated.get("total")).intValue() : null)
                .units(notYetRated.get("units") != null ? ((Number) notYetRated.get("units")).intValue() : null)
                .build();
    }

    private SellerDto.UnratedDto mapUnrated(Map<String, Object> unrated) {
        if (unrated == null) return null;
        return SellerDto.UnratedDto.builder()
                .paid(unrated.get("paid") != null ? ((Number) unrated.get("paid")).intValue() : null)
                .total(unrated.get("total") != null ? ((Number) unrated.get("total")).intValue() : null)
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.StatusDto mapStatus(Map<String, Object> status) {
        if (status == null) return null;
        return SellerDto.StatusDto.builder()
                .siteStatus((String) status.get("site_status"))
                .list(mapList((Map<String, Object>) status.get("list")))
                .buy(mapBuy((Map<String, Object>) status.get("buy")))
                .sell(mapSell((Map<String, Object>) status.get("sell")))
                .billing(mapBilling((Map<String, Object>) status.get("billing")))
                .mercadoenvios((String) status.get("mercadoenvios"))
                .mercadopagoTcAccepted((Boolean) status.get("mercadopago_tc_accepted"))
                .mercadopagoAccountType((String) status.get("mercadopago_account_type"))
                .immediatePayment((Boolean) status.get("immediate_payment"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.ListDto mapList(Map<String, Object> list) {
        if (list == null) return null;
        return SellerDto.ListDto.builder()
                .allow((Boolean) list.get("allow"))
                .codes((List<String>) list.get("codes"))
                .immediatePayment(mapImmediatePayment((Map<String, Object>) list.get("immediate_payment")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.BuyDto mapBuy(Map<String, Object> buy) {
        if (buy == null) return null;
        return SellerDto.BuyDto.builder()
                .allow((Boolean) buy.get("allow"))
                .codes((List<String>) buy.get("codes"))
                .immediatePayment(mapImmediatePayment((Map<String, Object>) buy.get("immediate_payment")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.SellDto mapSell(Map<String, Object> sell) {
        if (sell == null) return null;
        return SellerDto.SellDto.builder()
                .allow((Boolean) sell.get("allow"))
                .codes((List<String>) sell.get("codes"))
                .immediatePayment(mapImmediatePayment((Map<String, Object>) sell.get("immediate_payment")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.BillingDto mapBilling(Map<String, Object> billing) {
        if (billing == null) return null;
        return SellerDto.BillingDto.builder()
                .allow((Boolean) billing.get("allow"))
                .codes((List<String>) billing.get("codes"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private SellerDto.ImmediatePaymentDto mapImmediatePayment(Map<String, Object> immediatePayment) {
        if (immediatePayment == null) return null;
        return SellerDto.ImmediatePaymentDto.builder()
                .reasons((List<String>) immediatePayment.get("reasons"))
                .required((Boolean) immediatePayment.get("required"))
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
