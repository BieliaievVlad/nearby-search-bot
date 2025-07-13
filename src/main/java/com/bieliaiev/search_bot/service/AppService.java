package com.bieliaiev.search_bot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bieliaiev.search_bot.config.AppConfig;
import com.bieliaiev.search_bot.lang.StaticStrings;

@Service
public class AppService {

	private final AppConfig config;

	public AppService(AppConfig config) {
		this.config = config;
	}

	public void setupLanguage(long chatId, String text) {
		if (List.of(StaticStrings.RU, StaticStrings.EN, StaticStrings.UA).contains(text)) {
			config.setLanguage(chatId, text);
		}
	}
	
	public String getLanguage(long chatId) {
		return config.getLanguages().get(chatId);
	}
}
