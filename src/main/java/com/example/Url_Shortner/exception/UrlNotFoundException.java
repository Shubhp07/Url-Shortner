package com.example.Url_Shortner.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException{

    public UrlNotFoundException(String message){
        super(message);
    }
}
