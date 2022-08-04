package com.example.demo.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
//             ApiException apiException = new ApiException(ex.getMessage(),
//              status,
//              ZonedDateTime.now(ZoneId.of("Z")));
//        return super.handleExceptionInternal(ex, apiException, headers, status, request);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handlerApiRequestException(Exception e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
     ApiException apiException = new ApiException(e.getMessage(),
              httpStatus,
              ZonedDateTime.now(ZoneId.of("Z")));

      return new ResponseEntity<>(apiException,httpStatus);
    }

//    @ExceptionHandler(value = {ApiRequestException.class})
//    public ResponseEntity<Object> handlerIsAccountLockedException(LockedException e){
//        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        ApiException apiException = new ApiException(e.getMessage(),
//                httpStatus,
//                ZonedDateTime.now(ZoneId.of("Z")));
//
//        return new ResponseEntity<>(apiException,httpStatus);
//    }





}
