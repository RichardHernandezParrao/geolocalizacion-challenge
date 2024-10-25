package com.geolocalizacion.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeZoneDTO {
	private String name;
	private int offset;
	private int offsetWithDst;
	private String currentTime;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("offset")
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@JsonProperty("offset_with_dst")
	public int getOffsetWithDst() {
		return offsetWithDst;
	}

	public void setOffsetWithDst(int offsetWithDst) {
		this.offsetWithDst = offsetWithDst;
	}

	@JsonProperty("current_time")
	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
}