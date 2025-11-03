package com.example.Dailydone.ExceptionHandling;

import com.example.Dailydone.DTO.ExceptionDtO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> HandlingOtherExceptions(Exception ex, WebRequest webRequest){
        ExceptionDtO exceptionDTO = new ExceptionDtO();
        exceptionDTO.setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exceptionDTO);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException ex,
                                                     HttpServletRequest request) {
        System.out.println("Missing request param caught!");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Full Query String: " + request.getQueryString());
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Missing Param Name: " + ex.getParameterName());
        System.out.println("------------------------------------");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Missing parameter: " + ex.getParameterName());
    }

}
