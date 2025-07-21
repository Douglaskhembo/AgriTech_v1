package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.modal.Roles;
import com.KilimoConnectTech.repository.RolesRepository;
import com.KilimoConnectTech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ussd")
public class UssdController {

    private final UserService userService;
    private final RolesRepository rolesRepository;

    @PostMapping("/register")
    public String registerViaUssd(@RequestParam String sessionId,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String text) {

        String[] inputs = text.split("\\*");

        switch (inputs.length) {
            case 0:
            case 1:
                return "CON Enter your ID Number:";
            case 2:
                return "CON Enter your Full Name:";
            case 3:
                return "CON Enter your County:";
            case 4:
                return "CON Enter your Sub-County:";
            case 5:
                return "CON Enter Landmark:";
            case 6:
                // All info collected
                try {
                    UserDTO user = new UserDTO();
                    user.setIdNumber(inputs[1]);
                    user.setName(inputs[2]);
                    user.setCounty(inputs[3]);
                    user.setSubCounty(inputs[4]);
                    user.setLandMark(inputs[5]);
                    user.setPhoneNumber(phoneNumber);
                    Roles farmerRole = rolesRepository.findByName("FARMER");
                    user.setRoleId(farmerRole.getRoleId());

                    userService.createUser(user);
                    return "END Registration successful. Thank you!";
                } catch (Exception e) {
                    return "END Failed to register. Try again later.";
                }
            default:
                return "END Invalid input. Start again.";
        }
    }
}
