package com.geolocalizacion.challenge.controller;

import com.geolocalizacion.challenge.model.dto.GeolocationResponseDTO;
import com.geolocalizacion.challenge.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geolocation")
public class GeolocationController {

	@Autowired
	private GeolocationService geolocationService;

	@GetMapping("/{ip}")
	public GeolocationResponseDTO getGeolocalizacion(@PathVariable String ip) {
		return geolocationService.getGeolocationDataByIP(ip);
	}
}