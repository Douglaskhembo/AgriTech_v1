package com.KilimoConnectTech.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponse<T> {

    private String message;
    private Integer statusCode;
    private T data;

    public static <T> EntityResponse<T> ok(EntityResponse<T> entityResponse) {
        entityResponse.setStatusCode(HttpStatus.OK.value());
        return entityResponse;
    }

    public static <T> EntityResponse<T> status(HttpStatus httpStatus, EntityResponse<T> entityResponse) {
        entityResponse.setStatusCode(httpStatus.value());
        return entityResponse;
    }
}
