package com.bieliaiev.search_bot.bot;

import static org.junit.Assert.*;
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
import com.bieliaiev.search_bot.service.AppService;

@ExtendWith(MockitoExtension.class)
class SearchBotTest {
	
	@Mock
	private TelegramMessageHandler handler;
	
	@Mock
	private AppService service;
	
	@InjectMocks
	private SearchBot bot;
	
    @Test
    void onWebhookUpdateReceived_NoMessage_ReturnsNull() {
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(false);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertNull(result);
    }

    @Test
    void onWebhookUpdateReceived_LanguageIsNull_HandlesSetLanguage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(service.getLanguage(123L)).thenReturn(null);

        SendMessage expected = new SendMessage();
        when(handler.handleSetLanguageMessage(message, 123L)).thenReturn(expected);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertEquals(expected, result);
    }

    @Test
    void onWebhookUpdateReceived_StartCommand_HandlesStartCommand() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(service.getLanguage(123L)).thenReturn("ru");
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");

        SendMessage expected = new SendMessage();
        when(handler.handleStartCommand(123L)).thenReturn(expected);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertEquals(expected, result);
    }

    @Test
    void onWebhookUpdateReceived_TextMessage_HandlesTextMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(service.getLanguage(123L)).thenReturn("ru");
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("some query");

        SendMessage expected = new SendMessage();
        when(handler.handleTextMessage(message, 123L)).thenReturn(expected);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertEquals(expected, result);
    }

    @Test
    void onWebhookUpdateReceived_LocationMessage_HandlesLocationMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(service.getLanguage(123L)).thenReturn("ru");
        when(message.hasText()).thenReturn(false);
        when(message.hasLocation()).thenReturn(true);

        SendMessage expected = new SendMessage();
        when(handler.handleLocationMessage(message, 123L)).thenReturn(expected);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertEquals(expected, result);
    }

    @Test
    void onWebhookUpdateReceived_UnsupportedMessageType_ReturnsNull() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(service.getLanguage(123L)).thenReturn("ru");
        when(message.hasText()).thenReturn(false);
        when(message.hasLocation()).thenReturn(false);

        BotApiMethod<?> result = bot.onWebhookUpdateReceived(update);

        assertNull(result);
    }
}
