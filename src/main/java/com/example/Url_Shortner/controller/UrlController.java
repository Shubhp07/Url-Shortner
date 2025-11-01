package com.example.Url_Shortner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Url_Shortner.dto.ShortenUrlRequest;
import com.example.Url_Shortner.dto.ShortenUrlResponse;
import com.example.Url_Shortner.service.UrlShortenerService;

import jakarta.validation.Valid;

@RestController
public class UrlController {
    private final UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }
    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request){
        String shortCode = urlShortenerService.shortenUrl(request.url());

        String fullShortUrl = "http://localhost:8080/" + shortCode;

        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        return null;
    }


}
