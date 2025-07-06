package com.bieliaiev.search_bot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.bieliaiev.search_bot.config.GooglePlacesConfig;
import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.util.UrlBuilder;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GooglePlacesConfig config;

    @InjectMocks
    private SearchService service;
	
	@Test
	void testSearchNearby() {
		fail("Not yet implemented");
	}

	@Test
	void testFormatResults() {
		fail("Not yet implemented");
	}

	@Test
	void testHandleTextMessage() {
		fail("Not yet implemented");
	}

	@Test
	void testHandleLocationMessage() {
		fail("Not yet implemented");
	}
	
	@Test
	void getDetails_StatusIsOk_ReturnsExpectedResponse() {
		
		String placeId = "place_id";
		String url = "https://some-url.com";
		PlaceDetailsResponse expected = new PlaceDetailsResponse();
		PlaceDetailsResponse.Result result = new PlaceDetailsResponse.Result();
		expected.setResult(result);
		expected.setStatus("OK");
		
		when(urlBuilder.createGetDetailsUrl(anyString(), any(GooglePlacesConfig.class))).thenReturn(url);
		when(restTemplate.getForObject(anyString(), eq(PlaceDetailsResponse.class))).thenReturn(expected);
		
		PlaceDetailsResponse actual = service.getDetails(placeId);
		
		verify(urlBuilder, times(1)).createGetDetailsUrl(anyString(), any(GooglePlacesConfig.class));
		verify(restTemplate, times(1)).getForObject(anyString(), eq(PlaceDetailsResponse.class));
		assertThat(actual).isEqualTo(expected);
	}
	
    @Test
    void getDetails_StatusNotOk_ReturnsEmptyResponse() {
    	
        String placeId = "place_id";
        String url = "https://some-url.com";
        PlaceDetailsResponse failedResponse = new PlaceDetailsResponse();
        failedResponse.setStatus("ZERO_RESULTS");

        when(urlBuilder.createGetDetailsUrl(placeId, config)).thenReturn(url);
        when(restTemplate.getForObject(anyString(), eq(PlaceDetailsResponse.class))).thenReturn(failedResponse);

        PlaceDetailsResponse actual = service.getDetails(placeId);

        assertThat(actual.getResult()).isNull();
        verify(urlBuilder).createGetDetailsUrl(placeId, config);
        verify(restTemplate).getForObject(anyString(), eq(PlaceDetailsResponse.class));
    }

    @Test
    void getDetails_NullResponse_ReturnsEmptyResponse() {

        String placeId = "place_id";
        String url = "https://some-url.com";

        when(urlBuilder.createGetDetailsUrl(placeId, config)).thenReturn(url);
        when(restTemplate.getForObject(anyString(), eq(PlaceDetailsResponse.class))).thenReturn(null);

        PlaceDetailsResponse actual = service.getDetails(placeId);

        assertThat(actual).isNotNull();
        assertThat(actual.getResult()).isNull();
        verify(urlBuilder).createGetDetailsUrl(placeId, config);
        verify(restTemplate).getForObject(anyString(), eq(PlaceDetailsResponse.class));
    }
}
