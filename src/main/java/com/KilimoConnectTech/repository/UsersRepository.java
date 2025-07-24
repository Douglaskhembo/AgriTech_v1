package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.utils.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByIdNumber(String idNumber);
    boolean existsByEmail(String email);
    Users findByRole(RoleType roleType);
    Optional<Users> findByPhoneNumber(String phoneNumber);
}
