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

import com.bieliaiev.search_bot.handler.TelegramMessageHandler;

@ExtendWith(MockitoExtension.class)
class SearchBotTest {
	
	@Mock
	private TelegramMessageHandler handler;
	
	@InjectMocks
	private SearchBot bot;
	
	@Test
	void onWebhookUpdateReceived_TextMessage_ReturnsExpected() {
		
		long chatId = 12345L;
		Message message = new Message();
		SendMessage expected = new SendMessage(String.valueOf(chatId), "Text reply");

		when(handler.handleTextMessage(any(Message.class), anyLong())).thenReturn(expected);
		
		SendMessage actual = handler.handleTextMessage(message, chatId);
		
		verify(handler).handleTextMessage(any(Message.class), anyLong());
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void onWebhookUpdateReceived_LocationMessage_ReturnsExpected() {
		
		long chatId = 12345L;
		Message message = new Message();
		SendMessage expected = new SendMessage(String.valueOf(chatId), "Location reply");
		
		when(handler.handleLocationMessage(any(Message.class), anyLong())).thenReturn(expected);
		
		SendMessage actual = handler.handleLocationMessage(message, chatId);
		
		verify(handler).handleLocationMessage(any(Message.class), anyLong());
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void onWebhookUpdateReceived_EmptyUpdate_ReturnsNull() {
		
		BotApiMethod<?> actual = bot.onWebhookUpdateReceived(new Update());
		
		assertThat(actual).isNull();
	}

}
