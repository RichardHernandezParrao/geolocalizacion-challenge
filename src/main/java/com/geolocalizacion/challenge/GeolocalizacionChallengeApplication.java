package com.geolocalizacion.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeolocalizacionChallengeApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeolocalizacionChallengeApplication.class, args);
	}
}