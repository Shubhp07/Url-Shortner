package com.example.Url_Shortner.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record ShortenUrlRequest(
    @NotEmpty(message = "Url cannot be empty")
    @URL(message = "Invalid URL format")
        String url,
        String customAlias,
        @Min(value = 1, message = "Hours to expire must be a positive number")
    Integer hoursToExpire
        ) {

}
