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
public class ProductDTO {
    private Long prodId;
    private String prodName;
    private String unitPrice;
    private String unit;
    private String description;
    private String created_by;
    private Date creationDate;
    private String category;
}
