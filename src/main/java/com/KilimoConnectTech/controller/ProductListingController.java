package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.ProductListingDTO;
import com.KilimoConnectTech.repository.ProductListingRepository;
import com.KilimoConnectTech.service.ProductListService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductListingController {
    private final ProductListingRepository listingRepository;
    private final ProductListService listService;

    @PostMapping("/list/product")
    public Long listProduct(@RequestBody ProductListingDTO listRequest) throws BadRequestException {
        return listService.listedProduct(listRequest).getData();
    }
}
