package com.parkwoojin.musinsa.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(value = ExistSubCategoryException.class)
    public ErrorResponse handleExistSubCategoryException(ExistSubCategoryException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
