package com.KilimoConnectTech.service;

import com.KilimoConnectTech.dto.UserDTO;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.UsersRepository;
import com.KilimoConnectTech.utils.EntityResponse;
import com.KilimoConnectTech.utils.RegistrationType;
import com.KilimoConnectTech.utils.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public EntityResponse<Long> createUser(UserDTO request) throws BadRequestException {
        EntityResponse<Long> entityResponse = new EntityResponse<>();
        System.out.println("request>>> " +request);

        try {
            if ("USSD".equalsIgnoreCase(String.valueOf(request.getRegistrationType()))) {
                System.out.println("ussd passing >>>>>>>>>>>>>>>");
                if (request.getIdNumber() == null || request.getCounty() == null ||
                        request.getSubCounty() == null || request.getLandMark() == null ||
                        request.getName() == null) {
                    throw new BadRequestException("Missing required fields for USSD registration.");
                }
                System.out.println("pass all fields");

                if (usersRepository.existsByIdNumber(request.getIdNumber())) {
                    throw new BadRequestException("User with Id number already exists");
                }
                System.out.println("pass id check >>>>>>>");
                Users createdBy = usersRepository.findByRole(RoleType.ADMIN);
                System.out.println("Creation by " +createdBy);

                Users users = Users.builder()
                        .name(request.getName())
                        .idNumber(request.getIdNumber())
                        .status(true)
                        .createDate(new Date())
                        .landMark(request.getLandMark())
                        .county(request.getCounty())
                        .subCounty(request.getSubCounty())
                        .role(RoleType.FARMER)
                        .createdBy(createdBy)
                        .phoneNumber(request.getPhoneNumber())
                        .registrationType(request.getRegistrationType())
                        .build();

                Users createdUser = usersRepository.save(users);

                entityResponse.setMessage("USSD user created");
                entityResponse.setStatusCode(HttpStatus.CREATED.value());
                entityResponse.setData(createdUser.getUserId());

            } else {
                if (usersRepository.existsByIdNumber(request.getIdNumber())) {
                    throw new BadRequestException("User with Id number already exists");
                }

                if (usersRepository.existsByEmail(request.getEmail())) {
                    throw new BadRequestException("User with email already exists");
                }

                Users creator = usersRepository.findById(request.getUserId())
                        .orElseThrow(() -> new BadRequestException("Creator not found"));

                Users users = Users.builder()
                        .name(request.getName())
                        .idNumber(request.getIdNumber())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .status(true)
                        .createDate(new Date())
                        .landMark(request.getLandMark())
                        .company(request.getCompany())
                        .county(request.getCounty())
                        .subCounty(request.getSubCounty())
                        .role(RoleType.FARMER)
                        .phoneNumber(request.getPhoneNumber())
                        .createdBy(creator)
                        .registrationType(RegistrationType.WEB)
                        .build();

                Users createdUser = usersRepository.save(users);

                entityResponse.setMessage("Web user created");
                entityResponse.setStatusCode(HttpStatus.CREATED.value());
                entityResponse.setData(createdUser.getUserId());
            }

        } catch (Exception e) {
            log.error("Error while creating user: ", e);
            entityResponse.setMessage("User creation failed: " + e.getMessage());
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return entityResponse;
    }

    public EntityResponse<List<UserDTO>> getAllUsers() {
        EntityResponse<List<UserDTO>> response = new EntityResponse<>();
        try {
            response.setMessage("Fetched all users");
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(List.of()); // Replace with actual mapped DTOs later
        } catch (Exception e) {
            response.setMessage("Failed to fetch users: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public EntityResponse<Optional<UserDTO>> getUserById(Long userId) {
        EntityResponse<Optional<UserDTO>> response = new EntityResponse<>();
        try {
            response.setMessage("Fetched user by ID");
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(Optional.empty()); // Replace with actual user lookup
        } catch (Exception e) {
            response.setMessage("Error fetching user: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public EntityResponse<?> updateUser(Long userId, UserDTO userRequest, HttpServletRequest request) {
        EntityResponse<UserDTO> response = new EntityResponse<>();
        try {
            Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            // Add update logic or use BeanUtils.copyProperties if fields are directly mapped
            user.setName(userRequest.getName()); // Example field
            usersRepository.save(user);

            response.setMessage("Successfully updated User");
            response.setStatusCode(HttpStatus.ACCEPTED.value());
        } catch (Exception e) {
            log.error("Error updating User: ", e);
            response.setMessage("Failed to update user: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public EntityResponse<List<UserDTO>> getUsersByRole(String roleName) {
        EntityResponse<List<UserDTO>> response = new EntityResponse<>();
        try {
            // Replace with actual query and mapping
            response.setMessage("Fetched users by role");
            response.setStatusCode(HttpStatus.OK.value());
            response.setData(List.of());
        } catch (Exception e) {
            response.setMessage("Error fetching users by role: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    public UserDTO findByUserId(Long userId) {
        return null;
    }
}
