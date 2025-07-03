package com.bieliaiev.search_bot.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;

@Component
public class UrlBuilder {
	
	public String createNearbySearchUrl(NearbySearchParams params, GooglePlacesConfig config) {
		
        return UriComponentsBuilder.newInstance()
        		.scheme("https")
        		.host("maps.googleapis.com")
        		.path("/maps/api/place/nearbysearch/json")
        		.queryParam("location", params.getLocation().getLatitude() + "," + params.getLocation().getLongitude())
        		.queryParam("rankby", "distance")
        		.queryParam("keyword", params.getKeyword())
        		.queryParam("language", params.getLanguage())
        		.queryParam("key", config.getApiKey())
        		.build()
        		.toUriString();
	}
	
	public String createGetDetailsUrl(String placeId, GooglePlacesConfig config) {
		
		return UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("maps.googleapis.com")
				.path("/maps/api/place/details/json")
				.queryParam("place_id", placeId)
				.queryParam("key", config.getApiKey())
				.build()
				.toUriString();
	}
}
