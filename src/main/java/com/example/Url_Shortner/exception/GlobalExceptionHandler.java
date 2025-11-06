package com.example.Url_Shortner.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Object> handleUrlNotFoundException(UrlNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AliasAlreadyExistsException.class)
    public ResponseEntity<Object> handleAliasAlreadyExistsException(AliasAlreadyExistsException ex, WebRequest request) {

        // We create the same structured body as our other handlers for consistency.
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value()); // e.g., 409
        body.put("error", "Conflict"); // The standard reason phrase for a 409 status
        body.put("message", ex.getMessage()); // The specific message from our service layer
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Return the ResponseEntity, now with the 409 Conflict status.
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
