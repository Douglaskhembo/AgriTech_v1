package com.KilimoConnectTech.modal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_kc_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String phoneNumber;
    private String idNumber;
    private String county;
    private String subCounty;
    private String landMark;
    private String role;
    private String password;
    private String email;
    private Boolean status;
    private Date creation_date;
    private Date last_login;
    private String resetPassword;
    private String createdBy;
}
