package com.bieliaiev.search_bot.handler;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlacesResponse.Result;
import com.bieliaiev.search_bot.keyboard.KeyboardFactory;
import com.bieliaiev.search_bot.service.SearchService;
import com.bieliaiev.search_bot.util.KeywordFormatter;

@Service
public class TelegramMessageHandler {
	
	private final SearchService service;
	private final KeywordFormatter keywordFormatter;
	private final ConcurrentHashMap<Long, String> keywordCache = new ConcurrentHashMap<>();
	
	public TelegramMessageHandler(
			SearchService service,
			KeywordFormatter keywordFormatter) {
		this.service = service;
		this.keywordFormatter = keywordFormatter;
	}

	public SendMessage handleTextMessage(Message message, long chatId) {

		SendMessage reply = new SendMessage();
		String text = message.getText().toLowerCase().trim();
		String keyword = keywordFormatter.prepareKeyword(text);

		if (keyword.isBlank()) {
			return new SendMessage(String.valueOf(chatId),
					"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");			
		}
		keywordCache.put(chatId, keyword);
		reply.setChatId(chatId);
		reply.setText("Пожалуйста, поделитесь своим местоположением");
		reply.setReplyMarkup(KeyboardFactory.locationRequestKeyboard());
		
		return reply;		
	}
	
	public SendMessage handleLocationMessage(Message message, long chatId) {

		Location location = message.getLocation();
		if (location == null) {
		    return new SendMessage(String.valueOf(chatId), "Не удалось определить ваше местоположение.");
		}
		
		String keyword = keywordCache.get(chatId);

		if (keyword == null || keyword.isBlank()) {
			return new SendMessage(String.valueOf(chatId),
					"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
		}

		NearbySearchParams param = NearbySearchParams.builder().location(location).keyword(keyword).build();

		List<Result> result = service.searchNearby(param);
		SendMessage reply = new SendMessage(String.valueOf(chatId), service.formatResults(result));
		reply.setParseMode("HTML");
		
		keywordCache.remove(chatId);
		
		return reply;
	}
	
	public SendMessage handleStartCommand(long chatId) {
	    return new SendMessage(String.valueOf(chatId),
	            "Добро пожаловать! Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
	}
}
