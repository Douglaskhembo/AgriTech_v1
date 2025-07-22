package com.KilimoConnectTech.modal;


import com.KilimoConnectTech.utils.RegistrationType;
import com.KilimoConnectTech.utils.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private String password;
    private String company;
    private String email;
    private boolean status;
    private Date createDate;
    private Date last_login;
    private String resetPassword;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private Users createdBy;
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;
}
