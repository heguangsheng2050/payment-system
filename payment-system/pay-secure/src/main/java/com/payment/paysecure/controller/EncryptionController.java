package com.payment.paysecure.controller;

import com.payment.paysecure.model.EncryptionRequest;
import com.payment.paysecure.model.EncryptionResponse;
import com.payment.paysecure.service.EncryptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/secure")
@RequiredArgsConstructor
public class EncryptionController {

    private final EncryptionService encryptionService;

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponse> encrypt(@Valid @RequestBody EncryptionRequest request) {
        String encryptedData = encryptionService.encrypt(request.getData());
        return ResponseEntity.ok(new EncryptionResponse(encryptedData));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@Valid @RequestBody EncryptionRequest request) {
        String decryptedData = encryptionService.decrypt(request.getData());
        return ResponseEntity.ok(decryptedData);
    }
}