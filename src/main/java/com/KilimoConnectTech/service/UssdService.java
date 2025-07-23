package com.KilimoConnectTech.service;

import com.KilimoConnectTech.repository.UssdSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UssdService {

    private final UssdSessionRepository ussdSessionRepo;
}
