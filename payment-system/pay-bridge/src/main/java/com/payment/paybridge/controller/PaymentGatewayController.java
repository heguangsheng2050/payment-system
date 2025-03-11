package com.payment.paybridge.controller;

import com.payment.paybridge.model.PaymentRequest;
import com.payment.paybridge.model.PaymentResponse;
import com.payment.paybridge.service.PaymentGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService paymentGatewayService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentGatewayService.processPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("PayBridge service is healthy");
    }
}