package com.bieliaiev.search_bot.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.bieliaiev.search_bot.lang.MessageEN;
import com.bieliaiev.search_bot.lang.MessageKey;
import com.bieliaiev.search_bot.lang.MessageProvider;
import com.bieliaiev.search_bot.service.AppService;

@ExtendWith(MockitoExtension.class)
class ResultFormatterTest {

	@Mock
	private AppService service;
	
	@Mock
	private MessageProvider provider;
	
	@InjectMocks
	private ResultFormatter formatter;
	
	@Test
	void buildPlacesMessage_ValidInputData_ReturnsExpected() {
		
		long chatId = 123L;
		PlacesResponse.Result place = new PlacesResponse.Result();
		place.setName("name");
		place.setVicinity("vicinity");
		place.setRating(4.2);
		place.setUserRatingsTotal(10);
		PlaceDetailsResponse.Result result = new PlaceDetailsResponse.Result();
		result.setPhone("123456");
		result.setLocationUrl("locationUrl");
		PlaceDetailsResponse details = new PlaceDetailsResponse();
		details.setResult(result);

		String expected = """
		        🏪 name
		        📍 vicinity
		        📞123456
		        📌 <a href="locationUrl">Open on map</a>
		        ⭐ 4.2 (10 reviews)
		        
		        """;
		
		when(provider.prepareMessage(anyString(), eq(MessageKey.OPEN_ON_MAP))).thenReturn(MessageEN.OPEN_ON_MAP.getText());
		when(provider.prepareMessage(anyString(), eq(MessageKey.REVIEWS))).thenReturn(MessageEN.REVIEWS.getText());
		when(service.getLanguage(chatId)).thenReturn("EN").thenReturn("EN");

		String actual = formatter.buildPlacesMessage(place, details, chatId);
		
		verify(provider, times(2)).prepareMessage(anyString(), any());
		verify(service, times(2)).getLanguage(chatId);
		assertThat(actual).isEqualTo(expected);
	}

}
