package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.PaymentUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.PaymentRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.PaymentMethodResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Endpoints para gestión de pagos y métodos de pago")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    @GetMapping("/sites/{siteId}/payment_methods")
    @Operation(summary = "Obtener métodos de pago por sitio", description = "Retorna los métodos de pago disponibles para un sitio")
    public ResponseEntity<List<PaymentMethodResponse>> getPaymentMethodsBySite(
            @Parameter(description = "ID del sitio") @PathVariable String siteId) {
        List<PaymentMethodResponse> paymentMethods = paymentUseCase.getPaymentMethodsBySite(siteId);
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/sites/{siteId}/payment_methods/installments")
    @Operation(summary = "Obtener opciones de cuotas", description = "Retorna las opciones de cuotas disponibles")
    public ResponseEntity<List<PaymentMethodResponse>> getInstallmentOptions(
            @Parameter(description = "ID del sitio") @PathVariable String siteId,
            @Parameter(description = "Monto del pago") @RequestParam String amount,
            @Parameter(description = "ID del método de pago") @RequestParam String paymentMethodId) {
        List<PaymentMethodResponse> installments = paymentUseCase.getInstallmentOptions(siteId, amount, paymentMethodId);
        return ResponseEntity.ok(installments);
    }

    @PostMapping
    @Operation(summary = "Procesar pago", description = "Procesa un nuevo pago")
    public ResponseEntity<PaymentMethodResponse> processPayment(
            @RequestBody PaymentRequest request) {
        PaymentMethodResponse payment = paymentUseCase.processPayment(request);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Retorna información detallada de un pago")
    public ResponseEntity<PaymentMethodResponse> getPaymentById(
            @Parameter(description = "ID del pago") @PathVariable String id) {
        PaymentMethodResponse payment = paymentUseCase.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
}
