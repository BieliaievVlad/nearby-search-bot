package com.bieliaiev.search_bot.bot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import com.bieliaiev.search_bot.config.BotConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlacesResponse.Result;
import com.bieliaiev.search_bot.keyboard.KeyboardFactory;
import com.bieliaiev.search_bot.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class SearchBot implements WebhookBot {

	private final BotConfig config;
	private final SearchService service;
	private final ConcurrentHashMap<Long, String> keywordCache = new ConcurrentHashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(SearchBot.class);

	public SearchBot(BotConfig config, SearchService service) {
		this.config = config;
		this.service = service;
	}

	@Override
	public String getBotUsername() {
		return config.getBotUsername();
	}

	@Override
	public String getBotPath() {
		return config.getBotPath();
	}
	
	@Override
	public String getBotToken() {
		return config.getBotToken();
	}

	@Override
	public void setWebhook(SetWebhook setWebhook) throws TelegramApiException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

		if (!update.hasMessage()) {
			return null;
		}

		Message message = update.getMessage();
		long chatId = message.getChatId();

		if (message.hasText()) {
			String text = message.getText().toLowerCase().trim();
			String transliteratedText = transliterate(text);
			String keyword;
			if (transliteratedText.equals(text) || transliteratedText.isEmpty()) {
			    keyword = text;
			} else {
			    keyword = text + "|" + transliteratedText;
			}

			if (!keyword.isEmpty() && !keyword.isBlank()) {
				keywordCache.put(chatId, keyword);

				SendMessage reply = new SendMessage();
				reply.setChatId(chatId);
				reply.setText("Пожалуйста, поделитесь своим местоположением");
				reply.setReplyMarkup(KeyboardFactory.locationRequestKeyboard());

				return reply;

			} else {
				return new SendMessage(String.valueOf(chatId),
						"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
			}
			
		} else if (message.hasLocation()) {

			Location location = message.getLocation();
			String keyword = keywordCache.get(chatId);

			if (keyword == null || keyword.isBlank()) {
				return new SendMessage(String.valueOf(chatId),
						"Пожалуйста, введите что вы хотите найти. Например: суши или кафе.");
			}

			NearbySearchParams param = NearbySearchParams.builder().location(location).keyword(keyword).build();

			List<Result> result = service.searchNearby(param);
			keywordCache.remove(chatId);

			try {
				SendMessage sendMessage = new SendMessage(String.valueOf(chatId), service.formatResults(result));
				sendMessage.setParseMode("HTML");			
				return sendMessage;
				
			} catch (JsonProcessingException e) {
				logger.error("Failed to parse JSON response: {}", e.getMessage(), e);
				return new SendMessage(String.valueOf(chatId), "❌ Произошла ошибка при обработке ответа.");
			}
		}
		return null;
	}
	
	private String transliterate(String text) {
	    Map<Character, String> map = Map.ofEntries(
	        Map.entry('а', "a"), Map.entry('б', "b"), Map.entry('в', "v"),
	        Map.entry('г', "g"), Map.entry('ґ', "g"), Map.entry('д', "d"),
	        Map.entry('е', "e"), Map.entry('є', "ie"), Map.entry('ж', "zh"),
	        Map.entry('з', "z"), Map.entry('и', "y"), Map.entry('і', "i"),
	        Map.entry('ї', "i"), Map.entry('й', "i"), Map.entry('к', "k"),
	        Map.entry('л', "l"), Map.entry('м', "m"), Map.entry('н', "n"),
	        Map.entry('о', "o"), Map.entry('п', "p"), Map.entry('р', "r"),
	        Map.entry('с', "s"), Map.entry('т', "t"), Map.entry('у', "u"),
	        Map.entry('ф', "f"), Map.entry('х', "kh"), Map.entry('ц', "ts"),
	        Map.entry('ч', "ch"), Map.entry('ш', "sh"), Map.entry('щ', "shch"),
	        Map.entry('ю', "iu"), Map.entry('я', "ia"), Map.entry('ь', ""),
	        Map.entry('ъ', ""), Map.entry('э', "e"), Map.entry('ё', "e")
	    );

	    StringBuilder result = new StringBuilder();
	    for (char c : text.toLowerCase().toCharArray()) {
	        result.append(map.getOrDefault(c, String.valueOf(c)));
	    }
	    return result.toString();
	}
}
