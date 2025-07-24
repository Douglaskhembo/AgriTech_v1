package com.KilimoConnectTech.service;

import com.KilimoConnectTech.dto.ProductListingDTO;
import com.KilimoConnectTech.modal.ProductListing;
import com.KilimoConnectTech.modal.Products;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.ProductListingRepository;
import com.KilimoConnectTech.repository.ProductRepository;
import com.KilimoConnectTech.repository.UsersRepository;
import com.KilimoConnectTech.utils.EntityResponse;
import com.KilimoConnectTech.utils.RoleType;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductListService {

    private final ProductListingRepository listingRepo;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public EntityResponse<Long> listedProduct(ProductListingDTO listRequest) throws BadRequestException {
        EntityResponse<Long> response = new EntityResponse<>();

        Optional<Users> userOpt = usersRepository.findById(listRequest.getUserId());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("Invalid user ID: " + listRequest.getUserId());
        }

        Users buyer = usersRepository.findByRole(RoleType.BUYER);

        Optional<Products> productOpt = productRepository.findById(listRequest.getProdId());
        if (productOpt.isEmpty()) {
            throw new BadRequestException("Invalid product ID: " + listRequest.getProdId());
        }

        Products product = productOpt.get();

        if (!product.isInStock()) {
            throw new BadRequestException("Product '" + product.getProdName() + "' is not in stock.");
        }

        ProductListing prodlist = ProductListing.builder()
                .createdBy(userOpt.get())
                .product(product)
                .quantity(listRequest.getQuantity())
                .status(true)
                .listingDate(new Date())
                .buyer(buyer)
                .build();

        ProductListing listed = listingRepo.save(prodlist);

        response.setMessage("Product listed successfully");
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(listed.getListingId());

        return response;
    }
}
