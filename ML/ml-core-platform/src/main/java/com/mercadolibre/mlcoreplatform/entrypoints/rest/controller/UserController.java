package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.UserUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.UserSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.UserResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints para gestión de usuarios y vendedores")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna lista de usuarios con paginación")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<UserResponse> users = userUseCase.getAllUsers(offset, limit);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna información detallada de un usuario")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID del usuario") @PathVariable String id) {
        UserResponse user = userUseCase.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/items/search")
    @Operation(summary = "Obtener productos del usuario", description = "Retorna los productos publicados por el usuario")
    public ResponseEntity<List<ProductResponse>> getUserItems(
            @Parameter(description = "ID del usuario") @PathVariable String id,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<ProductResponse> products = userUseCase.getUserItems(id, offset, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/accepted_payment_methods")
    @Operation(summary = "Obtener métodos de pago aceptados", description = "Retorna los métodos de pago que acepta el usuario")
    public ResponseEntity<List<PaymentMethodResponse>> getUserPaymentMethods(
            @Parameter(description = "ID del usuario") @PathVariable String id) {
        List<PaymentMethodResponse> paymentMethods = userUseCase.getUserPaymentMethods(id);
        return ResponseEntity.ok(paymentMethods);
    }

    @PostMapping("/search")
    @Operation(summary = "Buscar usuarios", description = "Busca usuarios según criterios específicos")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestBody UserSearchRequest request) {
        List<UserResponse> users = userUseCase.searchUsers(request);
        return ResponseEntity.ok(users);
    }
}
