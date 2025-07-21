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
public class MarketPriceDTO {
    private Long priceId;
    private Long prodId;
    private Double pricePerKg;
    private Integer demandScore;
    private Date updatedAt;
}
