package com.bieliaiev.search_bot.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageProviderTest {

	@InjectMocks
	private MessageProvider provider;
	
	@Test
	void prepareMessage_LanguageIsRU_ReturnsExpected() {
		
		String language = "RU";
		
		String actual = provider.prepareMessage(language, MessageKey.WELCOME);
		
		assertThat(actual).isEqualTo(MessageRU.WELCOME.getText());
	}
	
	@Test
	void prepareMessage_LanguageIsEN_ReturnsExpected() {
		
		String language = "EN";
		
		String actual = provider.prepareMessage(language, MessageKey.WELCOME);
		
		assertThat(actual).isEqualTo(MessageEN.WELCOME.getText());
	}
	
	@Test
	void prepareMessage_LanguageIsUA_ReturnsExpected() {
		
		String language = "UA";
		
		String actual = provider.prepareMessage(language, MessageKey.WELCOME);
		
		assertThat(actual).isEqualTo(MessageUA.WELCOME.getText());
	}
	
	@Test
	void prepareMessage_UnsupportedLanguage_ReturnsExpected() {
		
		String language = "PL";
		String expected = "";
		
		String actual = provider.prepareMessage(language, MessageKey.WELCOME);
		
		assertThat(actual).isEqualTo(expected);
	}

}
