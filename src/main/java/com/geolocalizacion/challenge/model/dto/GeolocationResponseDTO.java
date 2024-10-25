package com.geolocalizacion.challenge.model.dto;

import com.geolocalizacion.challenge.deserializer.LanguagesDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GeolocationResponseDTO {

    @JsonProperty("country_code2")
    private String countryCode2;

    @JsonProperty("country_code3")
    private String countryCode3;

    @JsonProperty("country_name")
    private String countryName;

    @JsonDeserialize(using = LanguagesDeserializer.class)
    private String[] languages;

    @JsonProperty("timezones")
    private List<String> timezones;

    private CurrencyDTO currency;

    private double latitude;

    private double longitude;

    private double distanceFromBuenosAires;

    private Double exchangeRate;

    public String getCountryCode2() {
        return countryCode2;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getCountryCode3() {
        return countryCode3;
    }

    public void setCountryCode3(String countryCode3) {
        this.countryCode3 = countryCode3;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<String> timezones) {
        this.timezones = timezones;
    }

    public CurrencyDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDTO currency) {
        this.currency = currency;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistanceFromBuenosAires() {
        return distanceFromBuenosAires;
    }

    public void setDistanceFromBuenosAires(double distanceFromBuenosAires) {
        this.distanceFromBuenosAires = distanceFromBuenosAires;
    }

    public Double  getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double  exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}