package com.payment.paysecure.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class EncryptionResponse {
    private String encryptedData;
}