package com.springboot.advice;

import com.springboot.exception.BusinessLogicException;
import com.springboot.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
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

    // TODO GlobalExceptionAdvice 기능 추가 1
    @ExceptionHandler
    // ResponseEntity<>는 Spring에서 HTTP 응답을 수동으로 구성할 때 사용하는 클래스
    // ErrorResponse만 보내면 단순히 응답 본문(body)만 보냄
    // http 응답 상태코드랑 같이 보내려면 ResponseEntity 써야함
    // 그러고 응답 본문은 ErrorResponse가 되는 것
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException e) {
        // 전달된 ExceptionCode 객체에서 상태 코드와 메시지를 가져옴
        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());
        // ExceptionCode에서 가져온 상태(int)를 valueOf를 통해 HttpStatus 객체로 변환함
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }
    // TODO GlobalExceptionAdvice 기능 추가 2

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {

        // ErrorResponse 생성
        final ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);

        // ResponseEntity 반환
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // TODO GlobalExceptionAdvice 기능 추가 3

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NullPointerException e) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
