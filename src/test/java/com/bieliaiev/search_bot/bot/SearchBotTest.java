package com.bieliaiev.search_bot.bot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.bieliaiev.search_bot.service.SearchService;

@ExtendWith(MockitoExtension.class)
class SearchBotTest {

	@Mock
	private SearchService service;
	
	@Mock
	private Update update;
	
	@Mock
	private Message message;
	
	@InjectMocks
	private SearchBot bot;
	
	@Test
	void onWebhookUpdateReceived_TextMessage_ReturnsExpected() {
		
		long chatId = 12345L;
		SendMessage expected = new SendMessage(String.valueOf(chatId), "Text reply");

		when(service.handleTextMessage(any(Message.class), anyLong())).thenReturn(expected);
		
		SendMessage actual = service.handleTextMessage(message, chatId);
		
		verify(service).handleTextMessage(any(Message.class), anyLong());
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void onWebhookUpdateReceived_LocationMessage_ReturnsExpected() {
		
		long chatId = 12345L;
		SendMessage expected = new SendMessage(String.valueOf(chatId), "Location reply");
		
		when(service.handleLocationMessage(any(Message.class), anyLong())).thenReturn(expected);
		
		SendMessage actual = service.handleLocationMessage(message, chatId);
		
		verify(service).handleLocationMessage(any(Message.class), anyLong());
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void onWebhookUpdateReceived_EmptyUpdate_ReturnsNull() {
		
		BotApiMethod<?> actual = bot.onWebhookUpdateReceived(new Update());
		
		assertThat(actual).isNull();
	}

}
