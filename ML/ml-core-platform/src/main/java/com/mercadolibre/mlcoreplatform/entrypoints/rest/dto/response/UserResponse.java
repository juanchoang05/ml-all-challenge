package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserResponse(
        String id,
        String nickname,
        String firstName,
        String lastName,
        String email,
        String countryId,
        String identification,
        LocalDateTime registrationDate,
        SellerReputationResponse sellerReputation,
        BuyerReputationResponse buyerReputation,
        List<String> tags
) {
    
    @Builder
    public record SellerReputationResponse(
            String powerSellerStatus,
            Integer transactionsCanceled,
            Integer transactionsCompleted,
            String levelId
    ) {}
    
    @Builder
    public record BuyerReputationResponse(
            Integer canceledTransactions,
            Integer transactions
    ) {}
}
