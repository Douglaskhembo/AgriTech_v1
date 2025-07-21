package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    boolean existsByRoleName(String roleName);
    Roles findByRoleName(String roleName);
}
