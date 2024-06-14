package com.springboot.advice;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());

        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {
        final ErrorResponse response = ErrorResponse.of(e.getConstraintViolations());

        return response;
    }

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        System.out.println(e.getExceptionCode().getStatus());
        System.out.println(e.getMessage());

        // TODO GlobalExceptionAdvice 기능 추가 1
        final ErrorResponse response =
        ErrorResponse.of(e.getExceptionCode().getStatus(),e.getMessage(),null,null);
        return new ResponseEntity<>(response,HttpStatus.valueOf(e.getExceptionCode()
                .getStatus()));
    }

    // TODO GlobalExceptionAdvice 기능 추가 2

    @ExceptionHandler
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        String message="METHOD_NOT_ALLOWED";
        final ErrorResponse response =
                ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED.value(),message,null,null);
        return new ResponseEntity<>(response,HttpStatus.METHOD_NOT_ALLOWED);
    }

    // TODO GlobalExceptionAdvice 기능 추가 3
    @ExceptionHandler
    public ResponseEntity handleException(Exception e){

        final ErrorResponse response = ErrorResponse.of(
                ExceptionCode.INTERNAL_SERVER_ERROR.getStatus(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(),
                null,
                null
        );

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
