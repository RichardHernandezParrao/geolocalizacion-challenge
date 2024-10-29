package com.geolocalizacion.challenge.controller;

import com.geolocalizacion.challenge.model.dto.GeolocationResponseDTO;
import com.geolocalizacion.challenge.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import net.jodah.expiringmap.ExpiringMap;
import net.jodah.expiringmap.ExpirationPolicy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/geolocation")
@EnableAsync
public class GeolocationController {

    @Autowired
    private GeolocationService geolocationService;

    private static final int REQUEST_LIMIT = 1000; 
    private static final ExpiringMap<String, Integer> requestsMap = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();

    @GetMapping("/{ip}")
    @Cacheable(value = "geolocationCache", key = "#ip") 
    @Async 
    public CompletableFuture<GeolocationResponseDTO> getGeolocalizacion(@PathVariable String ip) {
        
        Integer requestCount = requestsMap.get(ip);
        if (requestCount == null) {
            requestsMap.put(ip, 1, 1, TimeUnit.MINUTES);
        } else if (requestCount >= REQUEST_LIMIT) {
            throw new RuntimeException("Too many requests");
        } else {
            requestsMap.put(ip, requestCount + 1, 1, TimeUnit.MINUTES);
        }

        return CompletableFuture.completedFuture(geolocationService.getGeolocationDataByIP(ip));
    }

    public String fallbackMethod(String ip) {
        return "Unable to retrieve geolocation data for IP: " + ip + ". Please try again later.";
    }
}
