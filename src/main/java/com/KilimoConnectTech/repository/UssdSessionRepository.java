package com.KilimoConnectTech.repository;

import com.KilimoConnectTech.modal.UssdSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UssdSessionRepository extends JpaRepository<UssdSession, Long> {
    Optional<UssdSession> findBySessionCode(String sessionId);
}
