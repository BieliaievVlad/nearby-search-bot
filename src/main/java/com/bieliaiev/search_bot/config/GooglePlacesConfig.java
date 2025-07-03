package com.bieliaiev.search_bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "google.places")
public class GooglePlacesConfig {

	private String apiKey;
}
