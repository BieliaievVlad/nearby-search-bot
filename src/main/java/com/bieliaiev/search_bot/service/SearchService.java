package com.bieliaiev.search_bot.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchService {

	private final RestTemplate restTemplate;
	private final GooglePlacesConfig config;
	
	public SearchService(GooglePlacesConfig config) {
		this.restTemplate = new RestTemplate();
		this.config = config;
	}
	
	public List<PlacesResponse.Result> searchNearby(NearbySearchParams params) {
		
	    try {
	        String url = UriComponentsBuilder.newInstance()
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
	        
	        String response = restTemplate.getForObject(url, String.class);

	        ObjectMapper mapper = new ObjectMapper();
	        PlacesResponse placesResponse = mapper.readValue(response, PlacesResponse.class);
	        
	        if ("OK".equals(placesResponse.getStatus())) {
	            return placesResponse.getResults();
	        } else {
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	public String formatResults(List<PlacesResponse.Result> results) throws JsonProcessingException {
	    if (results.isEmpty()) {
	        return "Ничего не найдено 😔";
	    }

	    StringBuilder sb = new StringBuilder();
	    int limit = Math.min(results.size(), 5);

	    for (int i = 0; i < limit; i++) {
	        PlacesResponse.Result place = results.get(i);
	        PlaceDetailsResponse details = getDetails(place.getPlaceId());
	        sb.append("🍣 ").append(escapeHtml(place.getName())).append("\n");
	        sb.append("📍 ").append(escapeHtml(place.getAddress())).append("\n");
	        sb.append("📞").append(escapeHtml(details.getResult().getPhone())).append("\n");
	        sb.append("📌 <a href=\"")
	        .append(details.getResult().getLocationUrl())
	        .append("\">Open on map</a>\n");

	        if (place.getRating() != null) {
	            sb.append("⭐ ").append(place.getRating());

	            if (place.getUserRatingsTotal() != null) {
	                sb.append(" (").append(escapeHtml(String.valueOf(place.getUserRatingsTotal()))).append(" отзывов)");
	            }

	            sb.append("\n");
	        }

	        sb.append("\n");
	    }

	    return sb.toString().trim();
	}
	
	private PlaceDetailsResponse getDetails(String placeId) throws JsonProcessingException {
		
		String url = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("maps.googleapis.com")
				.path("/maps/api/place/details/json")
				.queryParam("place_id", placeId)
				.queryParam("key", config.getApiKey())
				.build()
				.toUriString();
		
		String response = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		PlaceDetailsResponse detailsResponse = mapper.readValue(response, PlaceDetailsResponse.class);
		
		if ("OK".equals(detailsResponse.getStatus())) {
			return detailsResponse;
			
		} else {
			return new PlaceDetailsResponse();
		}
	}

	private String escapeHtml(String text) {
	    if (text == null) return "-";
	    return text.replace("&", "&amp;")
	               .replace("<", "&lt;")
	               .replace(">", "&gt;");
	}
}
