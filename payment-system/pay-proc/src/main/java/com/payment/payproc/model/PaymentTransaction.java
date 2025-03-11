package com.payment.payproc.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transdemo")
@Data
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_date", nullable = false)
    private LocalDateTime txDate;

    @Column(name = "tx_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType txType;

    @Column(name = "payment", columnDefinition = "jsonb")
    private String payment;

    @Column(name = "payer", columnDefinition = "jsonb")
    private String payer;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}