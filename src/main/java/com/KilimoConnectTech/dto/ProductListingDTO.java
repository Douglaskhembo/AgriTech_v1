package com.KilimoConnectTech.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListingDTO {
    private Long listingId;
    private Long userId;
    private Long prodId;
    private BigDecimal quantity;
    private boolean status;
    private Date listingDate;
    private Long buyer;
}
