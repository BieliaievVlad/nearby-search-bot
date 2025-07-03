package com.bieliaiev.search_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDetailsResponse {

	private String status;
	private Result result;
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Result {
		
		@JsonProperty("international_phone_number")
		private String phone;
		
		@JsonProperty("url")
		private String locationUrl;
	}
}