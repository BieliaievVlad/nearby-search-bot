package com.bieliaiev.search_bot.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Location;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.service.AppService;

@ExtendWith(MockitoExtension.class)
class UrlBuilderTest {

	@Mock
	private AppService service;
	
	@InjectMocks
	private UrlBuilder builder;
	
	@Test
	void createNearbySearchUrl_ValidInputData_ReturnsExpected() {

		long chatId = 123L;
		Location location = new Location();
		location.setLatitude(1.25);
		location.setLongitude(2.35);
		String keyword = "someKeyword";
		NearbySearchParams param = NearbySearchParams.builder().location(location).keyword(keyword).build();

		GooglePlacesConfig config = new GooglePlacesConfig();
		config.setApiKey("googleApiKey");

		String expected = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=1.25,2.35&rankby=distance&"
				+ "keyword=someKeyword&language=en&key=googleApiKey";

		try (MockedStatic<LanguageUtil> languageUtilMock = Mockito.mockStatic(LanguageUtil.class)) {

			languageUtilMock.when(() -> LanguageUtil.toApiCode(anyString())).thenReturn("en");
			when(service.getLanguage(chatId)).thenReturn("EN");

			String actual = builder.createNearbySearchUrl(param, config, chatId);

			verify(service, times(1)).getLanguage(chatId);
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Test
	void createGetDetailsUrl_ValidInputData_ReturnsExpected() {
		
		String placeId = "placeId";
		GooglePlacesConfig config = new GooglePlacesConfig();
		config.setApiKey("googleApiKey");
		
		String expected = "https://maps.googleapis.com/maps/api/place/details/json?place_id=placeId&key=googleApiKey";
		
		String actual = builder.createGetDetailsUrl(placeId, config);
		
		assertThat(actual).isEqualTo(expected);
	}

}
