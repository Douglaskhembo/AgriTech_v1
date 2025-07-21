package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.modal.Roles;
import com.KilimoConnectTech.modal.UssdSession;
import com.KilimoConnectTech.repository.RolesRepository;
import com.KilimoConnectTech.repository.UssdSessionRepository;
import com.KilimoConnectTech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UssdController {

    private final UserService userService;
    private final RolesRepository rolesRepository;
    private final UssdSessionRepository ussdSessionRepository;

    @PostMapping("/ussd/register")
    public String registerViaUssd(@RequestParam String sessionId,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String text) {

        // Clean and split user inputs
        String[] inputs = text == null || text.isEmpty() ? new String[0] : text.split("\\*");

        // Save USSD session for auditing
        UssdSession session = UssdSession.builder()
                .sessionCode(sessionId)
                .phoneNumber(phoneNumber)
                .createdAt(new Date())
                .lastMenu(text)
                .build();
        ussdSessionRepository.save(session);

        // Build registration flow
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
                    user.setIdNumber(inputs[0]);     // ID Number
                    user.setName(inputs[1]);         // Full Name
                    user.setCounty(inputs[2]);       // County
                    user.setSubCounty(inputs[3]);    // Sub-County
                    user.setLandMark(inputs[4]);     // Landmark
                    user.setPhoneNumber(phoneNumber);
                    user.setRegistrationType("USSD");

                    // Assign FARMER role
                    Roles farmerRole = rolesRepository.findByRoleName("FARMER");
                    if (farmerRole != null) {
                        user.setRoleId(farmerRole.getRoleId());
                    }

                    userService.createUser(user);
                    return "END Registration successful. Thank you for joining KilimoConnect!";
                } catch (Exception e) {
                    e.printStackTrace(); // Log the error
                    return "END Failed to register. Please try again later.";
                }
            default:
                return "END Invalid input. Please try again.";
        }
    }
}
