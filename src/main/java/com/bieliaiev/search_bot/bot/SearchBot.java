package com.bieliaiev.search_bot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import com.bieliaiev.search_bot.config.BotConfig;
import com.bieliaiev.search_bot.service.SearchService;

@Component
public class SearchBot implements WebhookBot {

	private final BotConfig config;
	private final SearchService service;

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
			return service.handleTextMessage(message, chatId);

		} else if (message.hasLocation()) {
			return service.handleLocationMessage(message, chatId);

		}
		return null;
	}
}