package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;

import java.util.List;

public interface UserApiPort {
    
    List<ProductResponse> getUserItems(String userId, int offset, int limit);
    
    List<PaymentMethodResponse> getUserPaymentMethods(String userId);
    
    List<UserResponse> searchUsers(UserSearchRequest request);
}
