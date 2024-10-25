package com.geolocalizacion.challenge.model.dto;

public enum LocationEnumDTO {
	BUENOS_AIRES(-34.6037, -58.3816);

	private final double latitude;
	private final double longitude;

	LocationEnumDTO(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}