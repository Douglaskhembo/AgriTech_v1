package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.ProductListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface ProductListingRepository extends JpaRepository<ProductListing, Long> {
    List<ProductListing> findByListingId(Long prodId);

    List<ProductListing> findByProduct_ProdId(Long prodId);
}
