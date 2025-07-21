package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String farmer);
    boolean existsByRoleName(String roleName);
}
