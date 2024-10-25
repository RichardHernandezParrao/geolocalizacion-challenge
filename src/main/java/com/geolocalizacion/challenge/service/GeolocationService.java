package com.geolocalizacion.challenge.service;

import com.geolocalizacion.challenge.model.dto.CountryResponseDTO;
import com.geolocalizacion.challenge.model.dto.CurrencyResponseDTO;
import com.geolocalizacion.challenge.model.dto.GeolocationResponseDTO;
import com.geolocalizacion.challenge.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.text.DecimalFormat;

@Service
public class GeolocationService {

    private static final int EARTH_RADIUS_KM = 6371;
    private final String API_GEOLOCATION_URL;
    private final String API_COUNTRY_URL;
    private final String API_FIXER_URL;

    private final LocationRepository locationRepository;
    private final StatisticsService statisticsService;
    private final RestTemplate restTemplate;

    public GeolocationService(LocationRepository locationRepository,
                              StatisticsService statisticsService,
                              @Value("${api.geolocation.url}") String apiGeolocationUrl,
                              @Value("${api.country.url}") String apiCountryUrl,
                              @Value("${api.fixer.url}") String apiFixerUrl) {
        this.locationRepository = locationRepository;
        this.statisticsService = statisticsService;
        this.restTemplate = new RestTemplate();
        this.API_GEOLOCATION_URL = apiGeolocationUrl;
        this.API_COUNTRY_URL = apiCountryUrl;
        this.API_FIXER_URL = apiFixerUrl;
    }

    public GeolocationResponseDTO getDataByIP(String ip) {
        String geolocationUrl = API_GEOLOCATION_URL + ip;
        GeolocationResponseDTO geolocation = restTemplate.getForObject(geolocationUrl, GeolocationResponseDTO.class);

        if (geolocation == null || geolocation.getCountryName() == null || geolocation.getCountryCode2() == null) {
            throw new RuntimeException("Could not retrieve valid geolocation information.");
        }

        enrichGeolocationWithCountryData(geolocation);
        enrichGeolocationWithCurrencyData(geolocation);

        double[] coordinatesBA = locationRepository.findBuenosAires();
        double distance = calculateDistance(coordinatesBA[0], coordinatesBA[1], geolocation.getLatitude(), geolocation.getLongitude());
        geolocation.setDistanceFromBuenosAires(distance);

        statisticsService.recordDistance(geolocation.getCountryName(), distance);

        return geolocation;
    }

    private void enrichGeolocationWithCountryData(GeolocationResponseDTO geolocation) {
        String countryCode = geolocation.getCountryCode2();
        String countryApiUrl = API_COUNTRY_URL + countryCode;

        CountryResponseDTO[] countryResponse = restTemplate.getForObject(countryApiUrl, CountryResponseDTO[].class);

        if (countryResponse != null && countryResponse.length > 0) {
            geolocation.setTimezones(countryResponse[0].getTimezones());
        }
    }

    private void enrichGeolocationWithCurrencyData(GeolocationResponseDTO geolocation) {
        String currencyCode = geolocation.getCurrency().getCode();
        String currencyApiUrl = API_FIXER_URL + "&symbols=" + currencyCode + ",USD";

        CurrencyResponseDTO currencyResponse = restTemplate.getForObject(currencyApiUrl, CurrencyResponseDTO.class);

        if (currencyResponse != null && currencyResponse.getRates() != null) {
            Double localCurrencyRate = currencyResponse.getRates().get(currencyCode);
            if (localCurrencyRate != null) {
                Double exchangeRateToUsd = 1 / localCurrencyRate; // currencyCode to USD

                DecimalFormat df = new DecimalFormat("#.####");
                String formattedExchangeRate = df.format(exchangeRateToUsd);

                geolocation.setExchangeRate(Double.parseDouble(formattedExchangeRate));
            } else {
                geolocation.setExchangeRate(null);
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDist = Math.toRadians(lat2 - lat1);
        double lonDist = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
