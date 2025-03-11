package com.payment.paybridge.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    private String currency = "USD";
    
    @NotBlank(message = "Transaction type is required")
    private String transactionType;
    
    private String merchantId;
    
    private String description;
}