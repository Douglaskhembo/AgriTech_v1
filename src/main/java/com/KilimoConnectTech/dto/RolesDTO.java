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
public class RolesDTO {
    private Long roleId;
    private String roleName;
    private Date createDate;
    private Long userId;
}
