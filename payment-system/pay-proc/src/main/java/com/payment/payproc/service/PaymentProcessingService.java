package com.payment.payproc.service;

import com.payment.payproc.model.PaymentTransaction;
import com.payment.payproc.model.TransactionStatus;
import com.payment.payproc.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    @Value("${services.pay-secure.url}")
    private String paySecureUrl;

    @Value("${services.pay-bridge.url}")
    private String payBridgeUrl;

    @Transactional
    public PaymentTransaction processPayment(PaymentTransaction transaction) {
        // Set initial status
        transaction.setTxDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.pending);
        
        // Save initial transaction
        transaction = transactionRepository.save(transaction);

        try {
            // Encrypt sensitive data using paySecure
            String encryptedPayment = encryptPaymentData(transaction.getPayment());
            transaction.setPayment(encryptedPayment);

            // Process payment through payBridge
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(transaction.getPayment(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                payBridgeUrl + "/api/v1/gateway/process",
                request,
                String.class
            );

            // Update transaction status based on response
            if (response.getStatusCode() == HttpStatus.OK && 
                response.getBody() != null && 
                response.getBody().contains("Success")) {
                transaction.setStatus(TransactionStatus.successful);
            } else {
                transaction.setStatus(TransactionStatus.failed);
            }

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.failed);
        }

        // Save updated transaction
        return transactionRepository.save(transaction);
    }

    private String encryptPaymentData(String paymentData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> request = new HttpEntity<>(paymentData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            paySecureUrl + "/api/v1/secure/encrypt",
            request,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to encrypt payment data");
    }
}