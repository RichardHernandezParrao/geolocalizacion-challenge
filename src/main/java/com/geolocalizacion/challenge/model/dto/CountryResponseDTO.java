package com.geolocalizacion.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CountryResponseDTO {
	
	@JsonProperty("timezones")
	private List<String> timezones;

	public List<String> getTimezones() {
		return timezones;
	}

	public void setTimezones(List<String> timezones) {
		this.timezones = timezones;
	}
}