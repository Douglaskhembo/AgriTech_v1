package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.modal.ProductListing;
import com.KilimoConnectTech.modal.Products;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.modal.UssdSession;
import com.KilimoConnectTech.repository.ProductListingRepository;
import com.KilimoConnectTech.repository.ProductRepository;
import com.KilimoConnectTech.repository.UssdSessionRepository;
import com.KilimoConnectTech.service.SmsService;
import com.KilimoConnectTech.service.UserService;
import com.KilimoConnectTech.utils.RegistrationType;
import com.KilimoConnectTech.utils.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UssdController {

    private final UserService userService;
    private final UssdSessionRepository ussdSessionRepository;
    private final SmsService smsService;
    private final ProductRepository productRepository;
    private final ProductListingRepository productListingRepository;

    @PostMapping("/ussd")
    public String handleUssd(@RequestParam String sessionId,
                             @RequestParam String phoneNumber,
                             @RequestParam String text) {

        UssdSession session = ussdSessionRepository.findBySessionCode(sessionId)
                .orElse(UssdSession.builder()
                        .sessionCode(sessionId)
                        .phoneNumber(phoneNumber)
                        .createdAt(new Date())
                        .currentPage(0)
                        .flowState("INIT")
                        .build());

        String[] inputs = text == null || text.isEmpty() ? new String[0] : text.split("\\*");
        String userInput = inputs.length > 0 ? inputs[inputs.length - 1] : "";

        // New session logic
        if ("INIT".equals(session.getFlowState())) {
            if (userService.isUserRegistered(phoneNumber)) {
                session.setFlowState("MAIN_MENU");
                ussdSessionRepository.save(session);
                return mainMenu();
            } else {
                session.setFlowState("REG_ID");
                ussdSessionRepository.save(session);
                return "CON Welcome to KilimoConnect!\nEnter your ID Number:";
            }
        }

        switch (session.getFlowState()) {
            case "REG_ID":
                session.setTempData(inputs[0]);
                session.setFlowState("REG_NAME");
                ussdSessionRepository.save(session);
                return "CON Enter your Full Name:";
            case "REG_NAME":
                session.setTempData(session.getTempData() + "," + inputs[1]);
                session.setFlowState("REG_COUNTY");
                ussdSessionRepository.save(session);
                return "CON Enter your County:";
            case "REG_COUNTY":
                session.setTempData(session.getTempData() + "," + inputs[2]);
                session.setFlowState("REG_SUB_COUNTY");
                ussdSessionRepository.save(session);
                return "CON Enter your Sub-County:";
            case "REG_SUB_COUNTY":
                session.setTempData(session.getTempData() + "," + inputs[3]);
                session.setFlowState("REG_LANDMARK");
                ussdSessionRepository.save(session);
                return "CON Enter Landmark:";
            case "REG_LANDMARK":
                try {
                    String[] data = (session.getTempData() + "," + inputs[4]).split(",");
                    UserDTO user = new UserDTO();
                    user.setIdNumber(data[0]);
                    user.setName(data[1]);
                    user.setCounty(data[2]);
                    user.setSubCounty(data[3]);
                    user.setLandMark(data[4]);
                    user.setPhoneNumber(phoneNumber);
                    user.setRegistrationType(RegistrationType.USSD);

                    userService.createUser(user);
                    session.setFlowState("MAIN_MENU");
                    session.setTempData(null);
                    ussdSessionRepository.save(session);

                    return mainMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "END Registration failed. Please try again later.";
                }
            case "MAIN_MENU":
                switch (userInput) {
                    case "1":
                        return "END To place an order, visit our website or call support at 1234.";
                    case "2":
                        return "END To sell a product, our agent will contact you shortly.";
                    case "3":
                        session.setFlowState("MARKET_PRICES_PRODUCTS");
                        session.setCurrentPage(0);
                        ussdSessionRepository.save(session);
                        return marketPrices(session, "");
                    case "4":
                        return "END On-Demand Products:\n- Organic Fertilizer\n- Hybrid Seeds\n- Pesticides";
                    default:
                        return mainMenu();
                }
            case "MARKET_PRICES_PRODUCTS":
                return marketPrices(session, userInput);
            case "MARKET_PRICES_BUYERS":
                return buyersView(session, userInput);
            default:
                return "END Invalid session state.";
        }
    }

    private String mainMenu() {
        return "CON Welcome to KilimoConnect. Choose an option:\n"
                + "1. Make Order\n"
                + "2. Sell Product\n"
                + "3. Market Prices\n"
                + "4. On-Demand Products";
    }

    private String marketPrices(UssdSession session, String input) {
        int pageSize = 6;
        List<Products> products = productRepository.findAll().stream()
                .filter(Products::isInStock)
                .toList();
        int total = products.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        int currentPage = session.getCurrentPage();

        if ("98".equals(input)) currentPage = Math.min(currentPage + 1, totalPages - 1);
        else if ("99".equals(input)) currentPage = Math.max(currentPage - 1, 0);
        else if ("00".equals(input)) {
            session.setFlowState("MAIN_MENU");
            session.setCurrentPage(0);
            ussdSessionRepository.save(session);
            return mainMenu();
        } else if (input.matches("\\d+")) {
            int selected = Integer.parseInt(input) - 1;
            int index = currentPage * pageSize + selected;
            if (index >= 0 && index < total) {
                Products selectedProduct = products.get(index);
                session.setSelectedProductId(selectedProduct.getProdId());
                session.setFlowState("MARKET_PRICES_BUYERS");
                session.setCurrentPage(0);
                ussdSessionRepository.save(session);
                return buyersView(session, "");
            }
        }

        session.setCurrentPage(currentPage);
        ussdSessionRepository.save(session);

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, total);
        List<Products> pageItems = products.subList(start, end);

        StringBuilder res = new StringBuilder("CON Select a product:\n");
        for (int i = 0; i < pageItems.size(); i++) {
            int globalIndex = currentPage * pageSize + i + 1;
            res.append(globalIndex).append(". ").append(pageItems.get(i).getProdName()).append("\n");
        }
        if (currentPage < totalPages - 1) res.append("98. Next\n");
        if (currentPage > 0) res.append("99. Back\n");
        res.append("00. Home");
        return res.toString();
    }

    private String buyersView(UssdSession session, String input) {
        int pageSize = 6;
        Long productId = session.getSelectedProductId();
        Products product = productRepository.findById(productId).orElse(null);
        if (product == null) return "END Product not found.";

        List<ProductListing> listings = productListingRepository.findByProduct_ProdId(productId).stream()
                .filter(ProductListing::isStatus)
                .toList();
        System.out.println("listings >>>>>>>>>>>>>>>>" +listings);
        int total = listings.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        int currentPage = session.getCurrentPage();

        if ("98".equals(input)) currentPage++;
        else if ("99".equals(input)) currentPage = Math.max(currentPage - 1, 0);
        else if ("00".equals(input)) {
            session.setFlowState("MAIN_MENU");
            session.setCurrentPage(0);
            ussdSessionRepository.save(session);
            return mainMenu();
        } else if ("#".equals(input)) {
            session.setFlowState("MARKET_PRICES_PRODUCTS");
            session.setCurrentPage(0);
            ussdSessionRepository.save(session);
            return marketPrices(session, "");
        }

        session.setCurrentPage(currentPage);
        ussdSessionRepository.save(session);

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, total);
        List<ProductListing> pageItems = listings.subList(start, end);

        StringBuilder res = new StringBuilder("CON Buyers for ")
                .append(product.getProdName()).append(":\n");

        for (int i = 0; i < pageItems.size(); i++) {
            ProductListing listing = pageItems.get(i);
            Users buyer = listing.getBuyer();
            String buyerName = (buyer != null && buyer.getName() != null) ? buyer.getName() : "Unknown";

            res.append(i + 1).append(". ")
                    .append(product.getProdName()).append(" - ")
                    .append(listing.getCurrency()).append(" ")
                    .append(listing.getUnitPrice()).append(" per ")
                    .append(listing.getUnit()).append(" by ")
                    .append(buyerName).append("\n");
        }

        if (currentPage > 0) res.append("99. Back\n");
        if (currentPage < totalPages - 1) res.append("98. Next\n");
        res.append("#. Products\n00. Home");

        return res.toString();
    }

}
