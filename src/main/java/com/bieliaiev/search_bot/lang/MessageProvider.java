package com.bieliaiev.search_bot.lang;

import org.springframework.stereotype.Service;

@Service
public class MessageProvider {
	
	public String prepareMessage(String language, MessageKey key) {
		if(language.equals("RU")) {
			return MessageRU.valueOf(key.name()).getText();
		}
		else if(language.equals("EN")) {
			return MessageEN.valueOf(key.name()).getText();
		}
		else if(language.equals("UA")) {
			return MessageUA.valueOf(key.name()).getText();
		}
		return StaticStrings.BLANK;		
	}
}
