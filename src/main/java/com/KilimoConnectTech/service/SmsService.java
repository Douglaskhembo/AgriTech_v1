package com.KilimoConnectTech.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {


//    @Value("${africastalking.username}")
//    private String username;
//
//    @Value("${africastalking.apiKey}")
//    private String apiKey;
//
//    private SMS sms;
//
//    @PostConstruct
//    public void init() {
//        AfricasTalking.initialize(username, apiKey);
//        sms = AfricasTalking.getService(SMS.class);
//    }
//
//    public void sendSms(String phoneNumber, String message) {
//        try {
//            sms.send(message, new String[]{phoneNumber}, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
