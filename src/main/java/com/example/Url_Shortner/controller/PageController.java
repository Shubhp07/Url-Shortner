package com.example.Url_Shortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Url_Shortner.dto.UrlStatsResponse;
import com.example.Url_Shortner.exception.AliasAlreadyExistsException;
import com.example.Url_Shortner.exception.UrlNotFoundException;
import com.example.Url_Shortner.service.UrlShortenerService;

@Controller
public class PageController {

    private final UrlShortenerService urlShortenerService;

    public PageController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, @RequestParam(name = "customAlias", required = false) String customAlias,
            Model model) {
        model.addAttribute("originalUrl", longUrl);

        try {
            // UPDATED: Pass both the URL and the (potentially null) alias to the service.
            String shortCode = urlShortenerService.shortenUrl(longUrl, customAlias,null);
            String fullShortUrl = "http://localhost:8080/" + shortCode;

            // Add the successful result to the model.
            model.addAttribute("shortUrlResult", fullShortUrl);

        } catch (AliasAlreadyExistsException e) {
            // If the service throws our custom exception, we catch it.
            // We add a user-friendly error message to the model for Thymeleaf to display.
            model.addAttribute("aliasError", e.getMessage());
        }
        
        return "index";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model) {
        try {
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            model.addAttribute("urlStats", stats);
        } catch (UrlNotFoundException e) {
            model.addAttribute("statsError", "Statistics not found for short code: " + shortCode);
        }
        return "index";
    }
}
