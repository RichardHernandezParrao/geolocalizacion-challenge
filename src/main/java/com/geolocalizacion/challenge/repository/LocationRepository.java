package com.geolocalizacion.challenge.repository;

import org.springframework.stereotype.Repository;

import com.geolocalizacion.challenge.model.dto.LocationEnumDTO;

@Repository
public class LocationRepository {
    public double[] findBuenosAires() {
        return new double[]{LocationEnumDTO.BUENOS_AIRES.getLatitude(), LocationEnumDTO.BUENOS_AIRES.getLongitude()};
    }
}

