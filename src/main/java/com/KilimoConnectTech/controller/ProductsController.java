package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.ProductDTO;
import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.service.ProductService;
import com.KilimoConnectTech.utils.EntityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductsController {

    private final ProductService productService;

    @PostMapping("/product/add")
    public Long createProducts(@RequestBody ProductDTO prodRequest) throws BadRequestException {
        return productService.createProducts(prodRequest).getData();
    }

    @PutMapping("/product/update/{prodId}")
    public EntityResponse<?> updateProduct(@PathVariable Long prodId, @RequestBody ProductDTO prodRequest, HttpServletRequest request) {
        return productService.updateProduct(prodId, prodRequest,request);
    }

    @GetMapping("/product/view")
    public EntityResponse<List<ProductDTO>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/view/{prodId}")
    public EntityResponse<List<ProductDTO>> getUsersByRole(@PathVariable Long prodId) {
        return productService.findByProId(prodId);
    }

    @DeleteMapping("/product/delete/{prodId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long prodId) {
        return productService.deleteProductById(prodId);
    }

}
