package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;

import java.util.List;

public interface UserUseCase {
    
    List<UserResponse> getAllUsers(int offset, int limit);
    
    UserResponse getUserById(String userId);
    
    List<ProductResponse> getUserItems(String userId, int offset, int limit);
    
    List<PaymentMethodResponse> getUserPaymentMethods(String userId);
    
    List<UserResponse> searchUsers(UserSearchRequest request);
}
