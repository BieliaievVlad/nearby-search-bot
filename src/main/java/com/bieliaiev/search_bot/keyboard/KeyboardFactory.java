package com.bieliaiev.search_bot.keyboard;

import java.util.Collections;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyboardFactory {

	public ReplyKeyboardMarkup locationRequestKeyboard() {
		
		KeyboardButton locationButton = new KeyboardButton("Поделиться местоположением");
		locationButton.setRequestLocation(true);
		
		KeyboardRow row = new KeyboardRow();
		row.add(locationButton);
		
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		keyboard.setKeyboard(Collections.singletonList(row));
		keyboard.setResizeKeyboard(true);
		keyboard.setOneTimeKeyboard(true);
		
		return keyboard;
	} 
}
