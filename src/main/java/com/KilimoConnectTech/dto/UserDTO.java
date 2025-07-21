package com.KilimoConnectTech.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String name;
    private String phoneNumber;
    private String idNumber;
    private String county;
    private String subCounty;
    private String landMark;
    private Long roleId;
    private String password;
    private String company;
    private String email;
    private boolean status;
    private Date createDate;
    private Date last_login;
    private String resetPassword;
    private String registrationType;
}
