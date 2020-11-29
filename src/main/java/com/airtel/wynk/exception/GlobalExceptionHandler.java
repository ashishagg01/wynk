package com.airtel.wynk.exception;

import com.airtel.wynk.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ResponseDTO errorDetails = new ResponseDTO("failed","invalid input parameter");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}