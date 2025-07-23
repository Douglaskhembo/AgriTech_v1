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
public class ProductDTO {
    private Long prodId;
    private String prodName;
    private BigDecimal unitPrice;
    private String unit;
    private String description;
    private Long createdBy;
    private Date creationDate;
    private String category;
    private Date modificationDate;
    private String currency;
}
