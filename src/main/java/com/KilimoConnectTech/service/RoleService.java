package com.KilimoConnectTech.service;

import com.KilimoConnectTech.dto.RolesDTO;
import com.KilimoConnectTech.modal.Roles;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.RolesRepository;
import com.KilimoConnectTech.repository.UsersRepository;
import com.KilimoConnectTech.utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;

    public EntityResponse<Long> createRole(RolesDTO request) throws BadRequestException {
        EntityResponse<Long> entityResponse = new EntityResponse<>();

        try {
                if (rolesRepository.existsByRoleName(request.getRoleName())) {
                    throw new BadRequestException("Role Name already exists");
                }

            Users creator = usersRepository.findById(request.getUserId())
                    .orElseThrow(() -> new BadRequestException("Creator not found"));

                Roles roles = Roles.builder()
                        .roleName(request.getRoleName())
                        .createDate(request.getCreateDate())
                        .createdBy(creator)
                        .build();

                Roles saveRole = rolesRepository.save(roles);

                entityResponse.setMessage("Role created successfully");
                entityResponse.setStatusCode(HttpStatus.CREATED.value());
                entityResponse.setData(saveRole.getRoleId());

        } catch (Exception e) {
            log.error("Error while creating role: ", e);
            entityResponse.setMessage("Role creation failed: " + e.getMessage());
            entityResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return entityResponse;
    }
}
