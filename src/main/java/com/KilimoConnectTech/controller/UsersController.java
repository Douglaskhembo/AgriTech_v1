package com.KilimoConnectTech.controller;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.service.UserService;
import com.KilimoConnectTech.utils.EntityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UsersController {

    private final UserService userService;

    @PostMapping("/register")
    public Long createUser(@RequestBody UserDTO userRequest) throws BadRequestException {
        return userService.createUser(userRequest).getData();
    }

    @PutMapping("/updateUser/{userId}")
    public EntityResponse<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userRequest, HttpServletRequest request) {
        return userService.updateUser(userId, userRequest,request);
    }

    @GetMapping("/getAllUsers")
    public EntityResponse<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUsersByRole")
    public EntityResponse<List<UserDTO>> getUsersByRole(@RequestParam String roleName) {
        return userService.getUsersByRole(roleName);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDTO> getByUserId(@PathVariable Long userId) {
        UserDTO user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
}
