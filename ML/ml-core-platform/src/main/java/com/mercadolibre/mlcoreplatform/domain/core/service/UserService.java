package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.SellerDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.UserUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.SellerPersistencePort;
import com.mercadolibre.mlcoreplatform.domain.port.out.UserApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    
    private final SellerPersistencePort sellerPersistencePort;
    private final UserApiPort userApiPort;
    
    @Override
    public List<UserResponse> getAllUsers(int offset, int limit) {
        List<SellerDto> sellers = sellerPersistencePort.findAll();
        return sellers.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToUserResponse)
                .toList();
    }
    
    @Override
    public UserResponse getUserById(String userId) {
        return sellerPersistencePort.findById(Long.parseLong(userId))
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    @Override
    public List<ProductResponse> getUserItems(String userId, int offset, int limit) {
        return userApiPort.getUserItems(userId, offset, limit);
    }
    
    @Override
    public List<PaymentMethodResponse> getUserPaymentMethods(String userId) {
        return userApiPort.getUserPaymentMethods(userId);
    }
    
    @Override
    public List<UserResponse> searchUsers(UserSearchRequest request) {
        return userApiPort.searchUsers(request);
    }
    
    private UserResponse mapToUserResponse(SellerDto sellerDto) {
        return UserResponse.builder()
                .id(sellerDto.id().toString())
                .nickname(sellerDto.nickname())
                .firstName(sellerDto.firstName())
                .lastName(sellerDto.lastName())
                .email(sellerDto.email())
                .countryId(sellerDto.countryId())
                .registrationDate(sellerDto.registrationDate())
                .tags(sellerDto.tags())
                .build();
    }
}
