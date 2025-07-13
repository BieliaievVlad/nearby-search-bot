package com.bieliaiev.search_bot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.bieliaiev.search_bot.bot.SearchBot;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {
	
	@Mock
	private SearchBot bot;
	
	@InjectMocks
	private WebhookController controller;
	
	@Test
	void onUpdateReceived_ValidUpdate_ReturnsExpectedResponse() {
		
		Update update = new Update();
	    BotApiMethod<?> expected = (BotApiMethod<?>) mock(BotApiMethod.class);
		
	    when(bot.onWebhookUpdateReceived(any(Update.class))).thenAnswer(invocation -> expected);
		
		ResponseEntity<?> actual = controller.onUpdateReceived(update);
		
		verify(bot).onWebhookUpdateReceived(any(Update.class));
		assertThat(actual.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(actual.getBody()).isEqualTo(expected);
	}
}
