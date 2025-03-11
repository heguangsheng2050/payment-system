package com.payment.payproc.controller;

import com.payment.payproc.model.PaymentTransaction;
import com.payment.payproc.service.PaymentProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentProcessingService paymentProcessingService;

    @PostMapping("/process")
    public ResponseEntity<PaymentTransaction> processPayment(@Valid @RequestBody PaymentTransaction transaction) {
        PaymentTransaction processedTransaction = paymentProcessingService.processPayment(transaction);
        return ResponseEntity.ok(processedTransaction);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("PayProc service is healthy");
    }
}