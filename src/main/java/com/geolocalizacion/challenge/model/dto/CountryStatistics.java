package com.geolocalizacion.challenge.model.dto;

public class CountryStatistics {
    private String countryName;
    private double distance;
    private int invocationCount;

    public CountryStatistics(String countryName, double distance, int invocationCount) {
        this.countryName = countryName;
        this.distance = distance;
        this.invocationCount = invocationCount;
    }

    public String getCountryName() {
        return countryName;
    }

    public double getDistance() {
        return distance;
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public void incrementCount() {
        this.invocationCount++;
    }
}