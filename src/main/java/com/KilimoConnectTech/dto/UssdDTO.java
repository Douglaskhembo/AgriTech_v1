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
public class UssdDTO {
    private Long sessionId;
    private String sessionCode;
    private String phoneNumber;
    private Date createdAt;
    private String lastMenu;
}
