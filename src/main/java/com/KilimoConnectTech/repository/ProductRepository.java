package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Products, Long> {
}
