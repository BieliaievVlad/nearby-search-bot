package com.bieliaiev.search_bot.util;

import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LanguageUtil {

	private final Map<String, String> LANGUAGE_API_CODES = Map.of(
			"RU", "ru",
			"EN", "en",
			"UA", "uk"
			);
	
	public String toApiCode(String language) {
		return LANGUAGE_API_CODES.getOrDefault(language, "en");
	}
}
