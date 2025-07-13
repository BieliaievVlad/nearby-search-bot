package com.bieliaiev.search_bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.util.UrlBuilder;

@Service
public class PlaceDetailsService {
	
	private final RestTemplate restTemplate;
	private final UrlBuilder urlBuilder;
	private final GooglePlacesConfig config;
	private final Logger log = LoggerFactory.getLogger(PlaceDetailsService.class);
	
	public PlaceDetailsService(
			RestTemplate restTemplate,
			UrlBuilder urlBuilder,
			GooglePlacesConfig config) {
		this.restTemplate = restTemplate;
		this.urlBuilder = urlBuilder;
		this.config = config;
	}

	PlaceDetailsResponse getDetails(String placeId) {

		String url = urlBuilder.createGetDetailsUrl(placeId, config);
		PlaceDetailsResponse detailsResponse = restTemplate.getForObject(url, PlaceDetailsResponse.class);

		if (detailsResponse != null && "OK".equals(detailsResponse.getStatus())) {
			return detailsResponse;

		} else {
			log.warn("Get Details returned status: {}",
					detailsResponse == null ? "null response" : detailsResponse.getStatus());
			return new PlaceDetailsResponse();
		}
	}
}
