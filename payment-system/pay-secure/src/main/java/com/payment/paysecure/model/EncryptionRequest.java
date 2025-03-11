package com.payment.paysecure.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EncryptionRequest {
    @NotBlank(message = "Data to encrypt cannot be empty")
    private String data;
}