package com.example.Url_Shortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, Model model) {
        String shortCode = urlShortenerService.shortenUrl(longUrl);

        String fullShortUrl = "http://localhost:8080/" + shortCode;

        model.addAttribute("originalUrl", longUrl);
        model.addAttribute("shortUrlResult", fullShortUrl);
        return "index";
    }
}
