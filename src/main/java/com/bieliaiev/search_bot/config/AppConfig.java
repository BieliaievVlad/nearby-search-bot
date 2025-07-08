package com.bieliaiev.search_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	private static final int LIMIT = 5;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public int getLimit() {
		return LIMIT;
	}
}
