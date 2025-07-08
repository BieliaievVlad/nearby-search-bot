package com.bieliaiev.search_bot.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlacesResponse.Result;
import com.bieliaiev.search_bot.service.SearchService;
import com.bieliaiev.search_bot.util.KeywordFormatter;
import com.bieliaiev.search_bot.util.UrlBuilder;

@ExtendWith(MockitoExtension.class)
class TelegramMessageHandlerTest {

	@Mock
	private KeywordFormatter keywordFormatter;
	
	@Mock
	private SearchService service;
	
    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private RestTemplate restTemplate;
	
	@InjectMocks
	private TelegramMessageHandler handler;
	
    @Test
    void handleTextMessage_KeywordIsBlank_ReturnsPromptMessage() {

        Message message = new Message();
        message.setText("    ");
        long chatId = 123L;

        when(keywordFormatter.prepareKeyword(anyString())).thenReturn(" ");

        SendMessage response = handler.handleTextMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Пожалуйста, введите что вы хотите найти. Например: суши или кафе.", response.getText());
        assertNull(response.getReplyMarkup());
    }

    @Test
    void handleTextMessage_ValidKeyword_ReturnsLocationRequest() {

        Message message = new Message();
        message.setText("Суши");
        long chatId = 456L;

        when(keywordFormatter.prepareKeyword("суши")).thenReturn("суши");

        SendMessage response = handler.handleTextMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Пожалуйста, поделитесь своим местоположением", response.getText());
        assertNotNull(response.getReplyMarkup());
    }

    @Test
    void handleLocationMessage_LocationIsNull_ReturnsErrorMessage() {
        Message message = new Message();
        message.setLocation(null);
        long chatId = 1L;

        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Не удалось определить ваше местоположение.", response.getText());
    }

    @Test
    void handleLocationMessage_KeywordMissing_ReturnsPromptMessage() {
        Message message = new Message();
        Location location = new Location();
        message.setLocation(location);
        long chatId = 2L;

        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Пожалуйста, введите что вы хотите найти. Например: суши или кафе.", response.getText());
    }

    @Test
    void handleLocationMessage_ValidKeywordAndLocation_ReturnsFormattedMessage() {
        long chatId = 3L;
        String keyword = "sushi";

        Location location = new Location();
        location.setLatitude(50.45);
        location.setLongitude(30.52);

        Message message = new Message();
        message.setLocation(location);

        ConcurrentHashMap<Long, String> cache = new ConcurrentHashMap<>();
        cache.put(chatId, keyword);
        ReflectionTestUtils.setField(handler, "keywordCache", cache);

        List<Result> mockResults = List.of(new Result());
        String formattedText = "<b>🍣 Sushi Place</b>";

        when(service.searchNearby(any(NearbySearchParams.class))).thenReturn(mockResults);
        when(service.formatResults(mockResults)).thenReturn(formattedText);

        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals(formattedText, response.getText());
        assertEquals("HTML", response.getParseMode());
        assertFalse(cache.containsKey(chatId));
    }
    
    @Test
    void handleStartCommand_ReturnsWelcomeMessage() {
        long chatId = 12345L;

        SendMessage response = handler.handleStartCommand(chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Добро пожаловать! Пожалуйста, введите что вы хотите найти. Например: суши или кафе.", response.getText());
        assertNull(response.getReplyMarkup());
    }
}
