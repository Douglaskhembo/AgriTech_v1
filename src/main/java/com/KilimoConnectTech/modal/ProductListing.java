package com.KilimoConnectTech.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sys_kc_product_listings")
public class ProductListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private Users createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Products product;
    private BigDecimal quantity;
    private boolean status;
    private Date listingDate;
    private BigDecimal unitPrice;
    private String currency;
    private String unit;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer")
    @JsonIgnore
    private Users buyer;
}

