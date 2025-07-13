package com.bieliaiev.search_bot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	private static final int LIMIT = 5;
	private static final String PARSE_MODE = "HTML";
	private Map<Long, String> languages = new HashMap<>();
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public int getLimit() {
		return LIMIT;
	}
	
	public String getParseMode() {
		return PARSE_MODE;
	}
	
	public Map<Long, String> getLanguages() {
		return languages;
	}
	
	public void setLanguage(long chatId, String text) {
		languages.put(chatId, text);
	}
}
