package com.example.Url_Shortner.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

public record ShortenUrlRequest(
    @NotEmpty(message = "Url cannot be empty")
    @URL(message = "Invalid URL format")
        String url
        ) {

}
