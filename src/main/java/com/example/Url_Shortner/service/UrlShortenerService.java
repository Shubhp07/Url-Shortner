package com.example.Url_Shortner.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.Url_Shortner.exception.UrlNotFoundException;
import com.example.Url_Shortner.model.UrlMapping;
import com.example.Url_Shortner.repository.UrlMappingRepository;

import jakarta.transaction.Transactional;

@Service
public class UrlShortenerService {

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    @Transactional
    public String shortenUrl(String originalUrl) {

        UrlMapping urlMapping = new UrlMapping();

        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreationDate(LocalDateTime.now());

        UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

        String shortCode = encodeBase62(savedEntity.getId());

        savedEntity.setShortCode(shortCode);
        urlMappingRepository.save(savedEntity);

        return shortCode;
    }

    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode) {


            UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode).
            orElseThrow(() -> new UrlNotFoundException("Url not found for short code"+shortCode));
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);
            return urlMapping.getOriginalUrl();
        

        
    }

    private String encodeBase62(Long number) {
        if (number == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();

        long num = number;

        while (num > 0) {
            int remainder = (int) (num % 62);

            sb.append(BASE62_CHARS.charAt(remainder));

            num /= 62;
        }

        return sb.reverse().toString();

    }

}
