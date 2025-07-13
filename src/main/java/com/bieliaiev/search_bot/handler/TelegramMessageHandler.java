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
import com.bieliaiev.search_bot.lang.MessageKey;
import com.bieliaiev.search_bot.lang.MessageProvider;
import com.bieliaiev.search_bot.lang.StaticStrings;
import com.bieliaiev.search_bot.service.AppService;
import com.bieliaiev.search_bot.service.SearchService;
import com.bieliaiev.search_bot.util.KeywordFormatter;

@Service
public class TelegramMessageHandler {
	
	private final SearchService service;
	private final AppService appService;
	private final MessageProvider provider;
	private final ConcurrentHashMap<Long, String> keywordCache = new ConcurrentHashMap<>();
	
	public TelegramMessageHandler(
			SearchService service,
			AppService appService,
			MessageProvider provider) {
		this.service = service;
		this.appService = appService;
		this.provider = provider;
	}

	public SendMessage handleTextMessage(Message message, long chatId) {

		SendMessage reply = new SendMessage();
		String text = message.getText().toLowerCase().trim();
		String keyword = KeywordFormatter.prepareKeyword(text);

		if (keyword.isBlank()) {
			return new SendMessage(String.valueOf(chatId),
					provider.prepareMessage(appService.getLanguage(chatId), MessageKey.ENTER_SEARCH_QUERY));		
		}
		keywordCache.put(chatId, keyword);
		reply.setChatId(chatId);
		reply.setText(provider.prepareMessage(appService.getLanguage(chatId), MessageKey.SHARE_LOCATION));
		reply.setReplyMarkup(KeyboardFactory.locationRequestKeyboard());
		
		return reply;		
	}
	
	public SendMessage handleLocationMessage(Message message, long chatId) {

		Location location = message.getLocation();
		if (location == null) {
		    return new SendMessage(String.valueOf(chatId), 
		    		provider.prepareMessage(appService.getLanguage(chatId), MessageKey.UNABLE_TO_DETERMINE_LOCATION));
		}
		
		String keyword = keywordCache.get(chatId);

		if (keyword == null || keyword.isBlank()) {
			return new SendMessage(String.valueOf(chatId),
					provider.prepareMessage(appService.getLanguage(chatId), MessageKey.ENTER_SEARCH_QUERY));
		}

		NearbySearchParams param = NearbySearchParams.builder().location(location).keyword(keyword).build();

		List<Result> result = service.searchNearby(param, chatId);
		SendMessage reply = new SendMessage(String.valueOf(chatId), service.formatResults(result, chatId));
		reply.setParseMode(service.setupSendMessageParseMode());
		
		keywordCache.remove(chatId);
		
		return reply;
	}
	
	public SendMessage handleStartCommand(long chatId) {
	    return new SendMessage(String.valueOf(chatId),
	    		provider.prepareMessage(appService.getLanguage(chatId), MessageKey.WELCOME));
	}
	
	public SendMessage handleSetLanguageMessage(Message message, long chatId) {

		String text = message.getText();
        if (text != null && List.of("RU", "EN", "UA").contains(text.toUpperCase())) {
            appService.setupLanguage(chatId, text);
            return new SendMessage(String.valueOf(chatId), 
            		provider.prepareMessage(appService.getLanguage(chatId), MessageKey.LANGUAGE_SELECTED));
        }

        SendMessage chooseLang = new SendMessage();
        chooseLang.setChatId(chatId);
        chooseLang.setText(StaticStrings.CHOOSE_LANGUAGE);
        chooseLang.setReplyMarkup(KeyboardFactory.languageRequestKeyboard());
        return chooseLang;
	}
}
