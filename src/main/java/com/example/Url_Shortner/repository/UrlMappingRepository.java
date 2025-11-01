package com.example.Url_Shortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Url_Shortner.model.UrlMapping;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>{
    Optional<UrlMapping> findByShortCode(String shortCode);

}
