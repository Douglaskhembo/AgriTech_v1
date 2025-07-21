package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.RolesDTO;
import com.KilimoConnectTech.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RolesController {

    private final RoleService roleService;
    @PostMapping("/create/role")
    public Long createUser(@RequestBody RolesDTO roleRequest) throws BadRequestException {
        return roleService.createRole(roleRequest).getData();
    }

}
