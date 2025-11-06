package com.example.Url_Shortner.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.Url_Shortner.dto.UrlStatsResponse;
import com.example.Url_Shortner.exception.AliasAlreadyExistsException;
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
    public String shortenUrl(String originalUrl, String customAlias, Integer hoursToExpire) {

        // Case 1: A custom alias is provided.
        if (StringUtils.hasText(customAlias)) {
            if (urlMappingRepository.findByShortCode(customAlias).isPresent()) {
                throw new AliasAlreadyExistsException("Alias '" + customAlias + "' is already in use.");
            }

            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            newUrlMapping.setCreationDate(LocalDateTime.now());
            newUrlMapping.setShortCode(customAlias);

            // --- NEW LOGIC FOR EXPIRATION ---
            // If the user provided a TTL, calculate and set the expiration date.
            if (hoursToExpire != null) {
                newUrlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }
            // If hoursToExpire is null, the expirationDate field remains null (permanent link).
            // --- END NEW LOGIC ---

            urlMappingRepository.save(newUrlMapping);
            return customAlias;
        } // Case 2: No custom alias. Generate a code.
        else {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreationDate(LocalDateTime.now());

            // --- NEW LOGIC FOR EXPIRATION ---
            // We apply the same logic here for the generated code path.
            if (hoursToExpire != null) {
                urlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }
            // --- END NEW LOGIC ---

            UrlMapping savedEntity = urlMappingRepository.save(urlMapping);
            String shortCode = encodeBase62(savedEntity.getId());
            savedEntity.setShortCode(shortCode);
            urlMappingRepository.save(savedEntity);
            return shortCode;
        }
    }

    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode) {

        // First, find the entity. If it's not found at all, throw the exception as before.
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for short code: " + shortCode));

        if (urlMapping.getExpirationDate() != null && urlMapping.getExpirationDate().isBefore(LocalDateTime.now())) {

            throw new UrlNotFoundException("This link has expired and is no longer active.");

        }

        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);

        return urlMapping.getOriginalUrl();

    }

    public UrlStatsResponse getStats(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No statistics found for short code" + shortCode));

        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
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
