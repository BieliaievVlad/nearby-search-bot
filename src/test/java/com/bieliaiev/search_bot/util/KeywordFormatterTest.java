package com.bieliaiev.search_bot.util;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordFormatterTest {

	
	@Test
	void prepareKeyword_EnglishText_ReturnsExpected() {
		
		String input = "abcdefg";
		String expected = "abcdefg";
		
		String actual = KeywordFormatter.prepareKeyword(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void prepareKeyword_RussianText_ReturnsExpected() {
		
		String input = "абвгде";
		String expected = "абвгде|abvgde";
		
		String actual = KeywordFormatter.prepareKeyword(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void transliterate_EnglishText_ReturnsExpected() {
		
		String input = "SoMe TeXt";
		String expected = "some text";
		
		String actual = KeywordFormatter.transliterate(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void transliterate_RussianText_ReturnsExpected() {
		
		String input = "КаКоЙ-тО тЕксТ";
		String expected = "kakoi-to tekst";
		
		String actual = KeywordFormatter.transliterate(input);
		
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	void transliterate_UkrainianText_ReturnsExpected() {
		
		String input = "ЇжачОк";
		String expected = "izhachok";
		
		String actual = KeywordFormatter.transliterate(input);
		
		assertThat(actual).isEqualTo(expected);
	}
}
