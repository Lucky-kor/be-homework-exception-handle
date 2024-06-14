package com.springboot.response;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private int httpStatus;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> violationErrors;

    private ErrorResponse(final List<FieldError> fieldErrors,
                          final List<ConstraintViolationError> violationErrors,
                          final Integer httpStatus,
                          final String message) {
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null,null,null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations), null,null);
    }
    public static ErrorResponse of(BusinessLogicException businessLogicException){
        int httpStatus = businessLogicException.getExceptionCode().getStatus();
        String message = businessLogicException.getMessage();
        return new ErrorResponse(null,null, httpStatus,message);
    }
    public static ErrorResponse of(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        return new ErrorResponse(null,null,httpStatus.value(),httpStatus.getReasonPhrase());
    }
    public static ErrorResponse of(NullPointerException nullPointerException){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(null,null, httpStatus.value(), httpStatus.getReasonPhrase());
    }
    public static ErrorResponse of(String message, int httpStatus){
        return new ErrorResponse(null,null,httpStatus,message);
    }

    @Getter
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String reason;

        private FieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors =
                                                        bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                            "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class ConstraintViolationError {
        private String propertyPath;
        private Object rejectedValue;
        private String reason;

        private ConstraintViolationError(String propertyPath, Object rejectedValue,
                                   String reason) {
            this.propertyPath = propertyPath;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolationError> of(
                Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }
}