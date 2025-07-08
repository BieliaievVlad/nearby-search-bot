package com.bieliaiev.search_bot.service;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.bieliaiev.search_bot.config.AppConfig;
import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.bieliaiev.search_bot.util.KeywordFormatter;
import com.bieliaiev.search_bot.util.ResultFormatter;
import com.bieliaiev.search_bot.util.UrlBuilder;

@Service
public class SearchService {

	private final AppConfig appConfig;
	private final PlaceDetailsService placeDetailsService;
	private final GooglePlacesConfig googlePlacesConfig;
	private final RestTemplate restTemplate;
	private final UrlBuilder urlBuilder;
	private final ResultFormatter resultFormatter;
	private final Logger log = LoggerFactory.getLogger(SearchService.class);
	
	public SearchService(
			AppConfig appConfig,
			PlaceDetailsService placeDetailsService,
			RestTemplate restTemplate,
			GooglePlacesConfig googlePlacesConfig,
			UrlBuilder urlBuilder,
			ResultFormatter resultFormatter,
			KeywordFormatter keywordFormatter) {
		this.appConfig = appConfig;
		this.placeDetailsService = placeDetailsService;
		this.restTemplate = restTemplate;
		this.googlePlacesConfig = googlePlacesConfig;
		this.urlBuilder = urlBuilder;
		this.resultFormatter = resultFormatter;
	}
	
	public List<PlacesResponse.Result> searchNearby(NearbySearchParams params) {
	    try {
	        String url = urlBuilder.createNearbySearchUrl(params, googlePlacesConfig);
	        PlacesResponse placesResponse = restTemplate.getForObject(url, PlacesResponse.class);

	        if (placesResponse != null && "OK".equals(placesResponse.getStatus())) {
	            return placesResponse.getResults();
	        }

	        log.warn("Search nearby returned status: {}",
	                placesResponse == null ? "null response" : placesResponse.getStatus());

	    } catch (RestClientException e) {
	        log.error("Failed to search nearby", e);
	    }

	    return Collections.emptyList();
	}
	
	public String formatResults(List<PlacesResponse.Result> results) {
		
	    if (results.isEmpty()) {
	        return "Ничего не найдено 😔";
	    }

	    int limit = calculateLimit(results);
	    
	    StringBuilder sb = new StringBuilder();
	    	    
	    for (int i = 0; i < limit; i++) {
	        PlacesResponse.Result place = results.get(i);
	        PlaceDetailsResponse details = placeDetailsService.getDetails(place.getPlaceId());
	        sb.append(resultFormatter.buildPlacesMessage(place, details));
	    }

	    return sb.toString().trim();
	}
	
	private int calculateLimit(List<PlacesResponse.Result> results) {

	    if(results.size() < appConfig.getLimit()) {
	    	return results.size();
	    	
	    } else {
	    	return appConfig.getLimit();
	    }
	}
}