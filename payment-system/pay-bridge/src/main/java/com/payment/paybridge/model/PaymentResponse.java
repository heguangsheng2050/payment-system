package com.payment.paybridge.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String status;
    private String message;
    private String transactionId;
    private String processorResponse;
    private String processorResponseCode;
    private long timestamp;
}