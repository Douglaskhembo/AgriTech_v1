package com.KilimoConnectTech.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsLongDTO {

    private Long smsId;
    private String phoneNumber;
    private String message;
    private Date sentDate;
    private String status;
}
