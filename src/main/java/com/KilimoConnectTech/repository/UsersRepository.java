package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByIdNumber(String idNumber);
    boolean existsByEmail(String email);
}
