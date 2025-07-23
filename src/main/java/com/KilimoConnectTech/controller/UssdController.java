package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.modal.Products;
import com.KilimoConnectTech.modal.UssdSession;
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

                    // Send SMS Notification
                    //smsService.sendSms(phoneNumber, "Welcome to KilimoConnect! Your registration was successful.");

                    return "CON Registration successful!\n\nChoose an option:\n1. Make Order\n2. Sell Product\n3. Market Prices\n4. On-demand Products\n5. Exit";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "END Failed to register. Please try again later.";
                }
            default:
                return "END Invalid input. Please try again.";
        }
    }

    @PostMapping("/ussd/make-order")
    public String makeOrder(@RequestParam String sessionId,
                            @RequestParam String phoneNumber,
                            @RequestParam String text) {
        // Simulate menu
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
        List<Products> products = productRepository.findAll();

        if (products.isEmpty()) {
            return "END No market prices available at the moment.";
        }

        StringBuilder response = new StringBuilder("END Today's Market Prices:\n");
        for (Products product : products) {
            response.append("- ")
                    .append(product.getProdName())
                    .append(product.getCurrency())
                    .append(product.getUnitPrice())
                    .append("/kg\n");
        }

        return response.toString().trim();
    }

    @PostMapping("/ussd/on-demand-products")
    public String onDemandProducts(@RequestParam String sessionId,
                                   @RequestParam String phoneNumber,
                                   @RequestParam String text) {
        return "END On-Demand Products:\n- Organic Fertilizer\n- Hybrid Seeds\n- Pesticides";
    }
}
