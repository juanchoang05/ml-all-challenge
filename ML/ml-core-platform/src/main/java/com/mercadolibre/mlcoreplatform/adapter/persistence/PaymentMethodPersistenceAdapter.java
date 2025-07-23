package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PaymentMethodDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.PaymentMethodPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PaymentMethodPersistenceAdapter implements PaymentMethodPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, List<Map<String, Object>>> paymentMethodsData;

    public PaymentMethodPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        
        ClassPathResource resource = new ClassPathResource("payment-methods.json");
        this.paymentMethodsData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, List<Map<String, Object>>>>() {}
        );
    }

    @Override
    public List<PaymentMethodDto> findBySiteId(String siteId) {
        List<Map<String, Object>> sitePaymentMethods = paymentMethodsData.get(siteId);
        if (sitePaymentMethods == null) {
            return List.of();
        }
        
        return sitePaymentMethods.stream()
                .map(this::mapToPaymentMethodDto)
                .toList();
    }

    @Override
    public List<PaymentMethodDto> findAll() {
        return paymentMethodsData.values().stream()
                .flatMap(List::stream)
                .map(this::mapToPaymentMethodDto)
                .toList();
    }

    @Override
    public List<PaymentMethodDto> findByPaymentType(String siteId, String paymentType) {
        List<Map<String, Object>> sitePaymentMethods = paymentMethodsData.get(siteId);
        if (sitePaymentMethods == null) {
            return List.of();
        }
        
        return sitePaymentMethods.stream()
                .filter(method -> paymentType.equals(method.get("payment_type_id")))
                .map(this::mapToPaymentMethodDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private PaymentMethodDto mapToPaymentMethodDto(Map<String, Object> data) {
        return PaymentMethodDto.builder()
                .id((String) data.get("id"))
                .name((String) data.get("name"))
                .paymentTypeId((String) data.get("payment_type_id"))
                .status((String) data.get("status"))
                .secureThumbnail((String) data.get("secure_thumbnail"))
                .thumbnail((String) data.get("thumbnail"))
                .deferredCapture((String) data.get("deferred_capture"))
                .settings(mapSettings((List<Map<String, Object>>) data.get("settings")))
                .additionalInfoNeeded((List<String>) data.get("additional_info_needed"))
                .minAllowedAmount(data.get("min_allowed_amount") != null ? 
                    ((Number) data.get("min_allowed_amount")).longValue() : null)
                .maxAllowedAmount(data.get("max_allowed_amount") != null ? 
                    ((Number) data.get("max_allowed_amount")).longValue() : null)
                .accreditationTime(data.get("accreditation_time") != null ? 
                    ((Number) data.get("accreditation_time")).intValue() : null)
                .financialInstitutions(mapFinancialInstitutions((List<Map<String, Object>>) data.get("financial_institutions")))
                .processingModes((List<String>) data.get("processing_modes"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<PaymentMethodDto.SettingDto> mapSettings(List<Map<String, Object>> settings) {
        if (settings == null) return List.of();
        return settings.stream()
                .map(setting -> PaymentMethodDto.SettingDto.builder()
                        .bin(mapBin((Map<String, Object>) setting.get("bin")))
                        .cardNumber(mapCardNumber((Map<String, Object>) setting.get("card_number")))
                        .securityCode(mapSecurityCode((Map<String, Object>) setting.get("security_code")))
                        .build())
                .toList();
    }

    private PaymentMethodDto.BinDto mapBin(Map<String, Object> bin) {
        if (bin == null) return null;
        return PaymentMethodDto.BinDto.builder()
                .pattern((String) bin.get("pattern"))
                .installmentsPattern((String) bin.get("installments_pattern"))
                .exclusionPattern((String) bin.get("exclusion_pattern"))
                .build();
    }

    private PaymentMethodDto.CardNumberDto mapCardNumber(Map<String, Object> cardNumber) {
        if (cardNumber == null) return null;
        return PaymentMethodDto.CardNumberDto.builder()
                .validation((String) cardNumber.get("validation"))
                .length(cardNumber.get("length") != null ? 
                    ((Number) cardNumber.get("length")).intValue() : null)
                .build();
    }

    private PaymentMethodDto.SecurityCodeDto mapSecurityCode(Map<String, Object> securityCode) {
        if (securityCode == null) return null;
        return PaymentMethodDto.SecurityCodeDto.builder()
                .length(securityCode.get("length") != null ? 
                    ((Number) securityCode.get("length")).intValue() : null)
                .cardLocation((String) securityCode.get("card_location"))
                .mode((String) securityCode.get("mode"))
                .build();
    }

    private List<PaymentMethodDto.FinancialInstitutionDto> mapFinancialInstitutions(List<Map<String, Object>> institutions) {
        if (institutions == null) return List.of();
        return institutions.stream()
                .map(institution -> PaymentMethodDto.FinancialInstitutionDto.builder()
                        .id((String) institution.get("id"))
                        .description((String) institution.get("description"))
                        .build())
                .toList();
    }
}
