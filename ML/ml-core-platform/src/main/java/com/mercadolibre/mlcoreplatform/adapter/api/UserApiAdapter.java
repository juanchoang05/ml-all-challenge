package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.UserApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserApiAdapter implements UserApiPort {

    @Override
    public List<ProductResponse> getUserItems(String userId, int offset, int limit) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public List<PaymentMethodResponse> getUserPaymentMethods(String userId) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public List<UserResponse> searchUsers(UserSearchRequest request) {
        // Mock implementation for testing
        return new ArrayList<>();
    }
}
