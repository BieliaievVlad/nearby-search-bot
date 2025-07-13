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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlacesResponse.Result;
import com.bieliaiev.search_bot.lang.MessageEN;
import com.bieliaiev.search_bot.lang.MessageKey;
import com.bieliaiev.search_bot.lang.MessageProvider;
import com.bieliaiev.search_bot.lang.MessageRU;
import com.bieliaiev.search_bot.service.AppService;
import com.bieliaiev.search_bot.service.SearchService;
import com.bieliaiev.search_bot.util.KeywordFormatter;
import com.bieliaiev.search_bot.util.UrlBuilder;

@ExtendWith(MockitoExtension.class)
class TelegramMessageHandlerTest {
	
	@Mock
	private AppService appService;
	
	@Mock
	private MessageProvider provider;
	
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

		try (MockedStatic<KeywordFormatter> keywordFormatterMock = Mockito.mockStatic(KeywordFormatter.class)) {
			keywordFormatterMock.when(() -> KeywordFormatter.prepareKeyword(anyString())).thenReturn(" ");
			when(provider.prepareMessage(anyString(), eq(MessageKey.ENTER_SEARCH_QUERY)))
					.thenReturn(MessageRU.ENTER_SEARCH_QUERY.getText());
			when(appService.getLanguage(anyLong())).thenReturn("RU");

			SendMessage response = handler.handleTextMessage(message, chatId);

			verify(provider, times(1)).prepareMessage(anyString(), eq(MessageKey.ENTER_SEARCH_QUERY));
			verify(appService, times(1)).getLanguage(anyLong());
			assertEquals(String.valueOf(chatId), response.getChatId());
			assertEquals("Пожалуйста, введите что вы хотите найти. Например: банк", response.getText());
			assertNull(response.getReplyMarkup());
		}
	}

    @Test
    void handleTextMessage_ValidKeyword_ReturnsLocationRequest() {

        Message message = new Message();
        message.setText("Sushi");
        long chatId = 456L;

        try(MockedStatic<KeywordFormatter> keywordFormatterMock = Mockito.mockStatic(KeywordFormatter.class)) {
        	keywordFormatterMock.when(() -> KeywordFormatter.prepareKeyword(anyString())).thenReturn("sushi");
        	when(provider.prepareMessage(anyString(), eq(MessageKey.SHARE_LOCATION))).thenReturn(MessageEN.SHARE_LOCATION.getText());
        	when(appService.getLanguage(chatId)).thenReturn("EN");
        	
            SendMessage response = handler.handleTextMessage(message, chatId);

            assertEquals(String.valueOf(chatId), response.getChatId());
            assertEquals("Please share your location.", response.getText());
            assertNotNull(response.getReplyMarkup());
        }
    }

    @Test
    void handleLocationMessage_LocationIsNull_ReturnsErrorMessage() {
        Message message = new Message();
        message.setText("some text");
        message.setLocation(null);
        long chatId = 1L;
        
        when(provider.prepareMessage(anyString(), eq(MessageKey.UNABLE_TO_DETERMINE_LOCATION)))
        	.thenReturn(MessageEN.UNABLE_TO_DETERMINE_LOCATION.getText());
        when(appService.getLanguage(chatId)).thenReturn("EN");

        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Location could not be determined.", response.getText());
    }

    @Test
    void handleLocationMessage_KeywordMissing_ReturnsPromptMessage() {
        Message message = new Message();
        Location location = new Location();
        message.setLocation(location);
        long chatId = 2L;

        
        when(provider.prepareMessage(anyString(), eq(MessageKey.ENTER_SEARCH_QUERY)))
    		.thenReturn(MessageEN.ENTER_SEARCH_QUERY.getText());
        when(appService.getLanguage(chatId)).thenReturn("EN");
    
        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Please enter what you want to find. For example: bank", response.getText());
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

        when(service.searchNearby(any(NearbySearchParams.class), anyLong())).thenReturn(mockResults);
        when(service.formatResults(mockResults, chatId)).thenReturn(formattedText);
        when(service.setupSendMessageParseMode()).thenReturn("HTML");

        SendMessage response = handler.handleLocationMessage(message, chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals(formattedText, response.getText());
        assertEquals("HTML", response.getParseMode());
        assertFalse(cache.containsKey(chatId));
    }
    
    @Test
    void handleStartCommand_ReturnsWelcomeMessage() {
        long chatId = 12345L;

        when(provider.prepareMessage(anyString(), eq(MessageKey.WELCOME)))
    		.thenReturn(MessageEN.WELCOME.getText());
        when(appService.getLanguage(chatId)).thenReturn("EN");
        
        SendMessage response = handler.handleStartCommand(chatId);

        assertEquals(String.valueOf(chatId), response.getChatId());
        assertEquals("Welcome! Please enter what you want to find. For example: bank", response.getText());
        assertNull(response.getReplyMarkup());
    }
}
