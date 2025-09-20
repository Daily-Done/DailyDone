package com.example.Dailydone.ExceptionHandling;

import com.example.Dailydone.DTO.ExceptionDtO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> HandlingOtherExceptions(Exception ex, WebRequest webRequest){
        ExceptionDtO exceptionDTO = new ExceptionDtO(
                webRequest.getDescription(false),
                HttpStatus.NOT_ACCEPTABLE,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exceptionDTO);
    }
}
