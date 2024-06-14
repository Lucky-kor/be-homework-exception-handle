package com.springboot.advice;

import com.springboot.exception.BusinessLogicException;
import com.springboot.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

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
        int httpStatus = e.getExceptionCode().getStatus();
        String message = e.getMessage();
        System.out.println(httpStatus);
        System.out.println(message);

        // TODO GlobalExceptionAdvice 기능 추가 1

        return new ResponseEntity<>(ErrorResponse.of(message,httpStatus),
                HttpStatus.valueOf(httpStatus));
    }

    // TODO GlobalExceptionAdvice 기능 추가 2
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        //return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.METHOD_NOT_ALLOWED);

        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        final ErrorResponse response = ErrorResponse.of(httpStatus.getReasonPhrase(),httpStatus.value());
        return response;
    }

    // TODO GlobalExceptionAdvice 기능 추가 3
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e){
        //return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.INTERNAL_SERVER_ERROR);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.of(httpStatus.getReasonPhrase(), httpStatus.value());
        return response;
    }
}
