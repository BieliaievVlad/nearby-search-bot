package com.bieliaiev.search_bot.dto;

import java.util.List;

import com.bieliaiev.search_bot.lang.StaticStrings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesResponse {

	private List<Result> results;
	private String status;
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String name;

        @JsonProperty("formatted_address")
        private String formattedAddress;
        
        @JsonProperty("vicinity")
        private String vicinity;

        private Double rating;

        @JsonProperty("user_ratings_total")
        private Integer userRatingsTotal;

        @JsonProperty("place_id")
        private String placeId;
        
        public String getAddress() {
        	
        	if (formattedAddress != null && !formattedAddress.isEmpty()) {
        		return formattedAddress;
        		
        	} else if (vicinity != null && !vicinity.isEmpty()) {
        		return vicinity;
        	}
			return StaticStrings.DASH;
        }

    }
}
