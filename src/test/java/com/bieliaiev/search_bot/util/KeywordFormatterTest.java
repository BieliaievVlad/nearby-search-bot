package com.bieliaiev.search_bot.util;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordFormatterTest {

	@InjectMocks
	private KeywordFormatter formatter;
	
	@Test
	void prepareKeyword_EnglishText_ReturnsExpected() {
		
		String input = "abcdefg";
		String expected = "abcdefg";
		
		String actual = formatter.prepareKeyword(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void prepareKeyword_RussianText_ReturnsExpected() {
		
		String input = "абвгде";
		String expected = "абвгде|abvgde";
		
		String actual = formatter.prepareKeyword(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	

}
