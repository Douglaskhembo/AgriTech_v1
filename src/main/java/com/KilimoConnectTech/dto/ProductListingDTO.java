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
public class ProductListingDTO {
    private Long listingId;
    private Long userId;
    private Long prodId;
    private Double quantity;
    private String status;
    private Date listingDate;
}
