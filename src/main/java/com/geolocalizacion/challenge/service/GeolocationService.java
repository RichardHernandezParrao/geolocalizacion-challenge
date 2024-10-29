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
	private static final String AMPERSAND = "&";
	private static final String SYMBOLS = "symbols";
	private static final String EQUALS = "=";
	private static final String COMMA = ",";
	private static final String USD = "USD";
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.####");

	private final String apiGeolocationUrl;
	private final String apiCountryUrl;
	private final String apiFixerUrl;

	private final LocationRepository locationRepository;
	private final StatisticsService statisticsService;
	private final RestTemplate restTemplate;

	public GeolocationService(LocationRepository locationRepository, StatisticsService statisticsService,
			@Value("${api.geolocation.url}") String apiGeolocationUrl,
			@Value("${api.country.url}") String apiCountryUrl, @Value("${api.fixer.url}") String apiFixerUrl) {
		this.locationRepository = locationRepository;
		this.statisticsService = statisticsService;
		this.restTemplate = new RestTemplate();
		this.apiGeolocationUrl = apiGeolocationUrl;
		this.apiCountryUrl = apiCountryUrl;
		this.apiFixerUrl = apiFixerUrl;
	}

	public GeolocationResponseDTO getGeolocationDataByIP(String ip) {
		String geolocationUrl = apiGeolocationUrl + ip;
		GeolocationResponseDTO geolocationResponse = restTemplate.getForObject(geolocationUrl,
				GeolocationResponseDTO.class);

		if (geolocationResponse == null || geolocationResponse.getCountryName() == null
				|| geolocationResponse.getCountryCode2() == null) {
			throw new RuntimeException("Could not retrieve valid geolocation information.");
		}

		enrichGeolocationWithCountryData(geolocationResponse);
		enrichGeolocationWithCurrencyData(geolocationResponse);

		double[] buenosAiresCoordinates = locationRepository.findBuenosAires();
		double distanceToBuenosAires = calculateDistance(buenosAiresCoordinates[0], buenosAiresCoordinates[1],
				geolocationResponse.getLatitude(), geolocationResponse.getLongitude());
		geolocationResponse.setDistanceToBuenosAiresInKm(distanceToBuenosAires);

		statisticsService.recordDistance(geolocationResponse.getCountryName(), distanceToBuenosAires);

		return geolocationResponse;
	}

	private void enrichGeolocationWithCountryData(GeolocationResponseDTO geolocation) {
		String countryCode = geolocation.getCountryCode2();
		String countryApiUrl = apiCountryUrl + countryCode;

		CountryResponseDTO[] countryResponse = restTemplate.getForObject(countryApiUrl, CountryResponseDTO[].class);

		if (countryResponse != null && countryResponse.length > 0) {
			geolocation.setTimezones(countryResponse[0].getTimezones());
		}
	}

	private void enrichGeolocationWithCurrencyData(GeolocationResponseDTO geolocation) {
		String currencyCode = geolocation.getCurrency().getCode();
		String currencyApiUrl = apiFixerUrl + AMPERSAND + SYMBOLS + EQUALS + currencyCode + COMMA + USD;

		CurrencyResponseDTO currencyResponse = restTemplate.getForObject(currencyApiUrl, CurrencyResponseDTO.class);

		if (currencyResponse != null && currencyResponse.getRates() != null) {
			Double localCurrencyRate = currencyResponse.getRates().get(currencyCode);
			if (localCurrencyRate != null) {
				Double exchangeRateToUsd = 1 / localCurrencyRate;

				String formattedExchangeRate = DECIMAL_FORMAT.format(exchangeRateToUsd);

				geolocation.setExchangeRate(Double.parseDouble(formattedExchangeRate));
			} else {
				geolocation.setExchangeRate(null);
			}
		}
	}

	public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
		double latitudeDifference = Math.toRadians(latitude2 - latitude1);
		double longitudeDifference = Math.toRadians(longitude2 - longitude1);

		double haversineValue = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
				+ Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
						* Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);

		double centralAngle = 2 * Math.atan2(Math.sqrt(haversineValue), Math.sqrt(1 - haversineValue));

		return EARTH_RADIUS_KM * centralAngle;
	}
}
