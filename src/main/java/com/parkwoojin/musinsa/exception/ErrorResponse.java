package com.parkwoojin.musinsa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private Integer errorCode;
    private String errorMessage;
}
