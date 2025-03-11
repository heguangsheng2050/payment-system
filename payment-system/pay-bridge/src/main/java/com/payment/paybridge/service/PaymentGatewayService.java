package com.payment.paybridge.service;

import com.payment.paybridge.model.PaymentRequest;
import com.payment.paybridge.model.PaymentResponse;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentGatewayService {

    public PaymentResponse processPayment(PaymentRequest request) {
        // Simulate payment processing based on account number
        boolean isSuccess = isEvenAccountNumber(request.getAccountNumber());
        
        return PaymentResponse.builder()
            .status(isSuccess ? "Success" : "Failure")
            .message(isSuccess ? "Transaction is successful" : "Transaction failed")
            .transactionId(UUID.randomUUID().toString())
            .processorResponse(isSuccess ? "Approved" : "Declined")
            .processorResponseCode(isSuccess ? "00" : "05")
            .timestamp(System.currentTimeMillis())
            .build();
    }

    private boolean isEvenAccountNumber(String accountNumber) {
        // Remove any non-numeric characters
        String numericOnly = accountNumber.replaceAll("[^0-9]", "");
        if (numericOnly.isEmpty()) {
            return false;
        }
        // Get the last digit
        int lastDigit = Character.getNumericValue(numericOnly.charAt(numericOnly.length() - 1));
        return lastDigit % 2 == 0;
    }
}