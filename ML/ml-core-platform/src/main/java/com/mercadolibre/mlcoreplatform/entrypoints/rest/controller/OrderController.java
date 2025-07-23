package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.OrderUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.OrderRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints para gestión de órdenes y compras")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Retorna lista de órdenes con paginación")
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<OrderResponse> orders = orderUseCase.getAllOrders(offset, limit);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Retorna información detallada de una orden")
    public ResponseEntity<OrderResponse> getOrderById(
            @Parameter(description = "ID de la orden") @PathVariable String id) {
        OrderResponse order = orderUseCase.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @Operation(summary = "Crear nueva orden", description = "Crea una nueva orden de compra")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request) {
        OrderResponse order = orderUseCase.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de orden", description = "Actualiza el estado de una orden específica")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "ID de la orden") @PathVariable String id,
            @Parameter(description = "Nuevo estado") @RequestParam String status) {
        OrderResponse order = orderUseCase.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener órdenes por usuario", description = "Retorna las órdenes de un usuario específico")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
            @Parameter(description = "ID del usuario") @PathVariable String userId,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<OrderResponse> orders = orderUseCase.getOrdersByUser(userId, offset, limit);
        return ResponseEntity.ok(orders);
    }
}
