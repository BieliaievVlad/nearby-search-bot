package com.bieliaiev.search_bot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Location;
import com.bieliaiev.search_bot.config.AppConfig;
import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.NearbySearchParams;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.bieliaiev.search_bot.lang.MessageKey;
import com.bieliaiev.search_bot.lang.MessageProvider;
import com.bieliaiev.search_bot.lang.MessageRU;
import com.bieliaiev.search_bot.util.ResultFormatter;
import com.bieliaiev.search_bot.util.UrlBuilder;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@Mock
	private AppService appService;
	
	@Mock
	private MessageProvider provider;
	
    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GooglePlacesConfig googlePlacesConfig;
    
    @Mock
    private AppConfig appConfig;
    
    @Mock
    private ResultFormatter resultFormatter;
    
    @Mock
    private PlaceDetailsService placeDetailsService;

    @InjectMocks
    private SearchService service;
	
    @Test
    void searchNearby_SuccessfulResponse_ReturnsResults() {

    	NearbySearchParams params = NearbySearchParams.builder()
    		    .location(new Location())
    		    .keyword("sushi")
    		    .build();
        String url = "https://test-url";
        PlacesResponse.Result result = new PlacesResponse.Result();
        List<PlacesResponse.Result> expectedResults = List.of(result);
        long chatId = 123L;

        PlacesResponse response = new PlacesResponse();
        response.setStatus("OK");
        response.setResults(expectedResults);

        when(urlBuilder.createNearbySearchUrl(params, googlePlacesConfig, chatId)).thenReturn(url);
        when(restTemplate.getForObject(url, PlacesResponse.class)).thenReturn(response);

        List<PlacesResponse.Result> actualResults = service.searchNearby(params, chatId);

        verify(urlBuilder, times(1)).createNearbySearchUrl(params, googlePlacesConfig, chatId);
        verify(restTemplate, times(1)).getForObject(url, PlacesResponse.class);
        assertEquals(expectedResults, actualResults);
    }

    @Test
    void searchNearby_StatusNotOk_ReturnsEmptyList() {
    	NearbySearchParams params = NearbySearchParams.builder()
    		    .location(new Location())
    		    .keyword("sushi")
    		    .build();
        String url = "https://test-url";
        long chatId = 123L;

        PlacesResponse response = new PlacesResponse();
        response.setStatus("ZERO_RESULTS");

        when(urlBuilder.createNearbySearchUrl(params, googlePlacesConfig, chatId)).thenReturn(url);
        when(restTemplate.getForObject(url, PlacesResponse.class)).thenReturn(response);

        List<PlacesResponse.Result> results = service.searchNearby(params, chatId);

        verify(urlBuilder, times(1)).createNearbySearchUrl(params, googlePlacesConfig, chatId);
        verify(restTemplate, times(1)).getForObject(url, PlacesResponse.class);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchNearby_NullResponse_ReturnsEmptyList() {
    	NearbySearchParams params = NearbySearchParams.builder()
    		    .location(new Location())
    		    .keyword("sushi")
    		    .build();
        String url = "https://test-url";
        long chatId = 123L;

        when(urlBuilder.createNearbySearchUrl(params, googlePlacesConfig, chatId)).thenReturn(url);
        when(restTemplate.getForObject(url, PlacesResponse.class)).thenReturn(null);

        List<PlacesResponse.Result> results = service.searchNearby(params, chatId);

        verify(urlBuilder, times(1)).createNearbySearchUrl(params, googlePlacesConfig, chatId);
        verify(restTemplate, times(1)).getForObject(url, PlacesResponse.class);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchNearby_ThrowsRestClientException_ReturnsEmptyList() {
    	NearbySearchParams params = NearbySearchParams.builder()
    		    .location(new Location())
    		    .keyword("sushi")
    		    .build();
        String url = "https://test-url";
        long chatId = 123L;

        when(urlBuilder.createNearbySearchUrl(params, googlePlacesConfig, chatId)).thenReturn(url);
        when(restTemplate.getForObject(url, PlacesResponse.class))
                .thenThrow(new RestClientException("Connection error"));

        List<PlacesResponse.Result> results = service.searchNearby(params, chatId);

        verify(urlBuilder, times(1)).createNearbySearchUrl(params, googlePlacesConfig, chatId);
        verify(restTemplate, times(1)).getForObject(url, PlacesResponse.class);
        assertTrue(results.isEmpty());
    }
    
	@Test
	void formatResults_EmptyResultsList_ReturnsExpectedMessage() {
		
		String expected = "Ничего не найдено 😔";
		long chatId = 123L;
		
		when(provider.prepareMessage(anyString(), eq(MessageKey.NOTHING_FOUND))).thenReturn(MessageRU.NOTHING_FOUND.getText());
		when(appService.getLanguage(anyLong())).thenReturn("RU");
		
		String actual = service.formatResults(new ArrayList<PlacesResponse.Result>(), chatId);
		
		verify(provider, times(1)).prepareMessage(anyString(), eq(MessageKey.NOTHING_FOUND));
		verify(appService, times(1)).getLanguage(anyLong());
		assertThat(actual).isEqualTo(expected);
	}
	
    @Test
    void formatResults_NotEmptyResultList_ReturnsExpectedMessage() {

    	String expected = "🍣 Sushi Place";
        PlacesResponse.Result place = mock(PlacesResponse.Result.class);
        PlaceDetailsResponse details = mock(PlaceDetailsResponse.class);
        long chatId = 123L;
        
        when(appConfig.getLimit()).thenReturn(1);
        when(place.getPlaceId()).thenReturn("abc123");        
        when(placeDetailsService.getDetails("abc123")).thenReturn(details);
        when(resultFormatter.buildPlacesMessage(place, details, chatId)).thenReturn(expected);

        String actual = service.formatResults(List.of(place), chatId);

        verify(appConfig, times(2)).getLimit();
        verify(place, times(1)).getPlaceId();
        assertThat(actual).isEqualTo(expected);
    }
}
