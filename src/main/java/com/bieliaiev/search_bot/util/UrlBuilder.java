package com.bieliaiev.search_bot.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.service.AppService;

@Component
public class UrlBuilder {
	
	private final AppService service;
	
	public UrlBuilder(AppService service) {
		this.service = service;
	}
	
	public String createNearbySearchUrl(NearbySearchParams params, GooglePlacesConfig config, long chatId) {
		
        return UriComponentsBuilder.newInstance()
        		.scheme("https")
        		.host("maps.googleapis.com")
        		.path("/maps/api/place/nearbysearch/json")
        		.queryParam("location", params.getLocation().getLatitude() + "," + params.getLocation().getLongitude())
        		.queryParam("rankby", "distance")
        		.queryParam("keyword", params.getKeyword())
        		.queryParam("language", LanguageUtil.toApiCode(service.getLanguage(chatId)))
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
