package com.KilimoConnectTech.service;

import com.KilimoConnectTech.dto.ProductDTO;
import com.KilimoConnectTech.modal.Products;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.ProductRepository;
import com.KilimoConnectTech.repository.UsersRepository;
import com.KilimoConnectTech.utils.EntityResponse;
import com.KilimoConnectTech.utils.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public EntityResponse<Long> createProducts(ProductDTO request) throws BadRequestException {
        EntityResponse<Long> entityResponse = new EntityResponse<>();
        try {
            Users createdBy = usersRepository.findByRole(RoleType.ADMIN);

            Products products = Products.builder()
                    .prodName(request.getProdName())
                    .unitPrice(request.getUnitPrice())
                    .unit(request.getUnit())
                    .inStock(true)
                    .creationDate(new Date())
                    .createdBy(createdBy)
                    .category(request.getCategory())
                    .description(request.getDescription())
                    .build();

            Products addedProduct = productRepository.save(products);

            entityResponse.setMessage("Product created");
            entityResponse.setStatusCode(HttpStatus.CREATED.value());
            entityResponse.setData(addedProduct.getProdId());

        } catch (Exception e) {
            log.error("Error while creating product: ", e);
            entityResponse.setMessage("product creation failed: " + e.getMessage());
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return entityResponse;
    }

    public EntityResponse<List<ProductDTO>> getAllProducts() {
        EntityResponse<List<ProductDTO>> response = new EntityResponse<>();
        try {
            List<Products> productsList = productRepository.findAll();
            List<ProductDTO> productDTOs = productsList.stream().map(this::mapToDTO).collect(Collectors.toList());

            response.setMessage("Products fetched successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(productDTOs);
        } catch (Exception e) {
            log.error("Error fetching products: ", e);
            response.setMessage("Failed to fetch products: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public EntityResponse<List<ProductDTO>> findByProId(Long prodId) {
        EntityResponse<List<ProductDTO>> response = new EntityResponse<>();
        try {
            Optional<Products> product = productRepository.findById(prodId);
            if (product.isPresent()) {
                ProductDTO dto = mapToDTO(product.get());
                response.setData(List.of(dto));
                response.setMessage("Product found");
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setMessage("Product not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setData(List.of());
            }
        } catch (Exception e) {
            log.error("Error finding product by ID: ", e);
            response.setMessage("Failed to fetch product: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public EntityResponse<?> updateProduct(Long prodId, ProductDTO prodRequest, HttpServletRequest request) {
        EntityResponse<Object> response = new EntityResponse<>();
        Users modifiedBy = usersRepository.findByRole(RoleType.ADMIN);

        try {
            Optional<Products> existingProduct = productRepository.findById(prodId);
            if (existingProduct.isEmpty()) {
                response.setMessage("Product not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                return response;
            }

            Products product = existingProduct.get();
            product.setProdName(prodRequest.getProdName());
            product.setUnitPrice(prodRequest.getUnitPrice());
            product.setUnit(prodRequest.getUnit());
            product.setDescription(prodRequest.getDescription());
            product.setCategory(prodRequest.getCategory());
            product.setModificationDate(new Date());
            product.setModifiedBy(modifiedBy);

            Products updated = productRepository.save(product);

            response.setMessage("Product updated successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(mapToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating product: ", e);
            response.setMessage("Failed to update product: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

//    private ProductDTO mapToDTO(Products product) {
//        return ProductDTO.builder()
//                .prodId(product.getProdId())
//                .prodName(product.getProdName())
//                .unitPrice(product.getUnitPrice())
//                .unit(product.getUnit())
//                .description(product.getDescription())
//                .category(product.getCategory())
//                .build();
//    }

    public ResponseEntity<?> deleteProductById(Long prodId) {
        try {
            Optional<Products> product = productRepository.findById(prodId);
            if (product.isPresent()) {
                productRepository.deleteById(prodId);
                return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
        } catch (Exception e) {
            log.error("Error deleting product: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete product: " + e.getMessage());
        }
    }

    private ProductDTO mapToDTO(Products product) {
        return ProductDTO.builder()
                .prodId(product.getProdId())
                .prodName(product.getProdName())
                .unitPrice(product.getUnitPrice())
                .unit(product.getUnit())
                .description(product.getDescription())
                .category(product.getCategory())
                .creationDate(product.getCreationDate())
                .modificationDate(product.getModificationDate())
                .createdBy(product.getCreatedBy() != null ? product.getCreatedBy().getUserId() : null)
                .build();
    }


}
