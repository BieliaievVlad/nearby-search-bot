package com.bieliaiev.search_bot.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;

@ExtendWith(MockitoExtension.class)
class ResultFormatterTest {

	@InjectMocks
	private ResultFormatter formatter;
	
	@Test
	void buildPlacesMessage_ValidInputData_ReturnsExpected() {
		
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
		        🍣 name
		        📍 vicinity
		        📞123456
		        📌 <a href="locationUrl">Open on map</a>
		        ⭐ 4.2 (10 отзывов)
		        
		        """;

		String actual = formatter.buildPlacesMessage(place, details);
		
		assertThat(actual).isEqualTo(expected);
	}

}
