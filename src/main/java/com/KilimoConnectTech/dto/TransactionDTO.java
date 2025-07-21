package com.KilimoConnectTech.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long transId;
    private Long userId;
    private String phoneNumber;
    private String transCode;
    private Double amount;
    private Date transDate;
    private String paymentStatus;
}
