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
import com.KilimoConnectTech.service.UssdService;
import com.KilimoConnectTech.utils.RegistrationType;
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
    private final UssdService ussdService;
    private final ProductRepository productRepository;
    private final ProductListingRepository productListingRepository;

    @PostMapping("/ussd/register")
    public String registerViaUssd(@RequestParam String sessionId,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String text) {

        String[] inputs = text == null || text.isEmpty() ? new String[0] : text.split("\\*");

        UssdSession session = UssdSession.builder()
                .sessionCode(sessionId)
                .phoneNumber(phoneNumber)
                .createdAt(new Date())
                .lastMenu(text)
                .currentPage(0)
                .flowState("MAIN_MENU")
                .build();
        ussdSessionRepository.save(session);

        switch (inputs.length) {
            case 0:
                return "CON Welcome to KilimoConnect!\nEnter your ID Number:";
            case 1:
                return "CON Enter your Full Name:";
            case 2:
                return "CON Enter your County:";
            case 3:
                return "CON Enter your Sub-County:";
            case 4:
                return "CON Enter Landmark:";
            case 5:
                try {
                    UserDTO user = new UserDTO();
                    user.setIdNumber(inputs[0]);
                    user.setName(inputs[1]);
                    user.setCounty(inputs[2]);
                    user.setSubCounty(inputs[3]);
                    user.setLandMark(inputs[4]);
                    user.setPhoneNumber(phoneNumber);
                    user.setRegistrationType(RegistrationType.USSD);

                    userService.createUser(user);

                    // smsService.sendSms(phoneNumber, "Welcome to KilimoConnect! Your registration was successful.");

                    return mainMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "END Failed to register. Please try again later.";
                }
            default:
                return "END Invalid input. Please try again.";
        }
    }

    private String mainMenu() {
        return "CON Registration successful!\n\nChoose an option:\n1. Make Order\n2. Sell Product\n3. Market Prices\n4. On-demand Products\n5. Exit";
    }

    @PostMapping("/ussd/make-order")
    public String makeOrder(@RequestParam String sessionId,
                            @RequestParam String phoneNumber,
                            @RequestParam String text) {
        return "END To place an order, visit our website or call support at 1234.";
    }

    @PostMapping("/ussd/sell-product")
    public String sellProduct(@RequestParam String sessionId,
                              @RequestParam String phoneNumber,
                              @RequestParam String text) {
        return "END To sell a product, our agent will contact you shortly.";
    }

    @PostMapping("/ussd/market-prices")
    public String marketPrices(@RequestParam String sessionId,
                               @RequestParam String phoneNumber,
                               @RequestParam String text) {

        UssdSession session = ussdSessionRepository.findBySessionCode(sessionId)
                .orElse(UssdSession.builder()
                        .sessionCode(sessionId)
                        .phoneNumber(phoneNumber)
                        .createdAt(new Date())
                        .currentPage(0)
                        .flowState("MARKET_PRICES_PRODUCTS")
                        .build());

        String flow = session.getFlowState();
        int pageSize = 6;

        // Clean up empty input
        String[] inputs = text == null || text.isEmpty() ? new String[0] : text.split("\\*");
        String userInput = inputs.length > 0 ? inputs[inputs.length - 1] : "";

        if (flow.equals("MARKET_PRICES_PRODUCTS")) {
            List<Products> products = productRepository.findAll()
                    .stream()
                    .filter(Products::isInStock)
                    .toList();

            int total = products.size();
            int totalPages = (int) Math.ceil((double) total / pageSize);
            int currentPage = session.getCurrentPage();

            // Navigation
            if (userInput.equals("98")) currentPage = Math.min(currentPage + 1, totalPages - 1);
            else if (userInput.equals("99")) currentPage = Math.max(currentPage - 1, 0);
            else if (userInput.equals("00")) {
                session.setCurrentPage(0);
                session.setFlowState("MAIN_MENU");
                ussdSessionRepository.save(session);
                return mainMenu();
            } else if (userInput.matches("\\d+")) {
                int selectedIndex = Integer.parseInt(userInput) - 1;
                int start = currentPage * pageSize;
                if (selectedIndex >= 0 && selectedIndex < pageSize && start + selectedIndex < total) {
                    Products selectedProduct = products.get(start + selectedIndex);
                    session.setSelectedProductId(selectedProduct.getProdId());
                    session.setFlowState("MARKET_PRICES_BUYERS");
                    session.setCurrentPage(0);
                    ussdSessionRepository.save(session);
                    return getBuyersView(selectedProduct, 0, session);
                }
            }

            session.setCurrentPage(currentPage);
            ussdSessionRepository.save(session);

            // Paginated product list
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, total);
            List<Products> pageItems = products.subList(start, end);

            StringBuilder response = new StringBuilder("CON Select a product:\n");
            for (int i = 0; i < pageItems.size(); i++) {
                response.append(i + 1).append(". ").append(pageItems.get(i).getProdName()).append("\n");
            }
            if (currentPage < totalPages - 1) response.append("98. Next\n");
            if (currentPage > 0) response.append("99. Back\n");
            response.append("00. Home");

            return response.toString();

        } else if (flow.equals("MARKET_PRICES_BUYERS")) {
            Long productId = session.getSelectedProductId();
            Products product = productRepository.findById(productId).orElse(null);
            if (product == null) return "END Product not found.";

            int currentPage = session.getCurrentPage();

            if (userInput.equals("98")) currentPage++;
            else if (userInput.equals("99")) currentPage = Math.max(currentPage - 1, 0);
            else if (userInput.equals("00")) {
                session.setFlowState("MAIN_MENU");
                session.setCurrentPage(0);
                ussdSessionRepository.save(session);
                return mainMenu();
            } else if (userInput.equals("#")) {
                session.setFlowState("MARKET_PRICES_PRODUCTS");
                session.setCurrentPage(0);
                ussdSessionRepository.save(session);
                return marketPrices(sessionId, phoneNumber, ""); // reload product view
            }

            session.setCurrentPage(currentPage);
            ussdSessionRepository.save(session);

            return getBuyersView(product, currentPage, session);
        }

        return "END Invalid flow.";
    }


    @PostMapping("/ussd/on-demand-products")
    public String onDemandProducts(@RequestParam String sessionId,
                                   @RequestParam String phoneNumber,
                                   @RequestParam String text) {
        return "END On-Demand Products:\n- Organic Fertilizer\n- Hybrid Seeds\n- Pesticides";
    }



    private String getBuyersView(Products product, int page, UssdSession session) {
        List<ProductListing> listings = productListingRepository.findByListingId(product.getProdId())
                .stream()
                .filter(ProductListing::isStatus)
                .toList();        int total = listings.size();
        int pageSize = 6;
        int totalPages = (int) Math.ceil((double) total / pageSize);

        int start = page * pageSize;
        int end = Math.min(start + pageSize, total);
        List<ProductListing> pageItems = listings.subList(start, end);

        StringBuilder response = new StringBuilder("CON Buyers for ")
                .append(product.getProdName()).append(":\n");

        for (int i = 0; i < pageItems.size(); i++) {
            ProductListing listing = pageItems.get(i);
            Users buyer = listing.getBuyer();

            response.append(i + 1).append(". ")
                    .append(buyer.getName()).append(" - ")
                    .append(product.getCurrency()).append(" ")
                    .append(product.getUnitPrice()).append("/").append(product.getUnit()).append("\n");
        }

        if (page > 0) response.append("99. Back\n");
        if (page < totalPages - 1) response.append("98. Next\n");
        response.append("#. Products\n");
        response.append("00. Home");

        return response.toString();
    }


}
