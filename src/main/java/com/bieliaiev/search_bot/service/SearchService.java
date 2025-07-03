package com.bieliaiev.search_bot.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse.Result;
import com.bieliaiev.search_bot.keyboard.KeyboardFactory;
import com.bieliaiev.search_bot.util.KeywordFormatter;
import com.bieliaiev.search_bot.util.ResultFormatter;
import com.bieliaiev.search_bot.util.UrlBuilder;

@Service
public class SearchService {

	private final RestTemplate restTemplate;
	private final GooglePlacesConfig config;
	private final UrlBuilder urlBuilder;
	private final ResultFormatter resultFormatter;
	private final KeywordFormatter keywordFormatter;
	private final ConcurrentHashMap<Long, String> keywordCache = new ConcurrentHashMap<>();
	private final Logger log = LoggerFactory.getLogger(SearchService.class);
	
	public SearchService(
			GooglePlacesConfig config,
			UrlBuilder urlBuilder,
			ResultFormatter resultFormatter,
			KeywordFormatter keywordFormatter) {
		this.restTemplate = new RestTemplate();
		this.config = config;
		this.urlBuilder = urlBuilder;
		this.resultFormatter = resultFormatter;
		this.keywordFormatter = keywordFormatter;
	}
	
	public List<PlacesResponse.Result> searchNearby(NearbySearchParams params) {

		try {
			String url = urlBuilder.createNearbySearchUrl(params, config);
			PlacesResponse placesResponse = restTemplate.getForObject(url, PlacesResponse.class);

			if (placesResponse != null && "OK".equals(placesResponse.getStatus())) {
				return placesResponse.getResults();

			} else {
				log.warn("Search nearby returned status: {}", 
						placesResponse == null ? "null response" : placesResponse.getStatus());
				return Collections.emptyList();
			}

		} catch (Exception e) {
			log.error("Failed to search nearby", e);
			return Collections.emptyList();
		}
	}
	
	public String formatResults(List<PlacesResponse.Result> results) {
		
	    if (results.isEmpty()) {
	        return "Ничего не найдено 😔";
	    }

	    StringBuilder sb = new StringBuilder();
	    int limit = Math.min(results.size(), 5);

	    for (int i = 0; i < limit; i++) {
	        PlacesResponse.Result place = results.get(i);
	        PlaceDetailsResponse details = getDetails(place.getPlaceId());
	        sb.append(resultFormatter.buildPlacesMessage(place, details));
	    }

	    return sb.toString().trim();
	}
	
	private PlaceDetailsResponse getDetails(String placeId) {
		
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
	
	public SendMessage handleTextMessage(Message message, long chatId) {

		SendMessage reply = new SendMessage();
		String text = message.getText().toLowerCase().trim();
		String keyword = keywordFormatter.prepareKeyword(text);

		if (!keyword.isEmpty() && !keyword.isBlank()) {
			
			keywordCache.put(chatId, keyword);
			reply.setChatId(chatId);
			reply.setText("Пожалуйста, поделитесь своим местоположением");
			reply.setReplyMarkup(KeyboardFactory.locationRequestKeyboard());
			
		} else {
			return new SendMessage(String.valueOf(chatId),
					"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
		}
		return reply;		
	}
	
	public SendMessage handleLocationMessage(Message message, long chatId) {

		Location location = message.getLocation();
		String keyword = keywordCache.get(chatId);

		if (keyword == null || keyword.isBlank()) {
			return new SendMessage(String.valueOf(chatId),
					"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
		}

		NearbySearchParams param = NearbySearchParams.builder().location(location).keyword(keyword).build();

		List<Result> result = searchNearby(param);
		SendMessage reply = new SendMessage(String.valueOf(chatId), formatResults(result));
		reply.setParseMode("HTML");
		
		keywordCache.remove(chatId);
		
		return reply;
	}
}