package com.bieliaiev.search_bot.keyboard;

import java.util.Collections;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.bieliaiev.search_bot.lang.StaticStrings;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyboardFactory {

	public ReplyKeyboardMarkup locationRequestKeyboard() {
		
		KeyboardButton locationButton = new KeyboardButton(StaticStrings.LOCATION_BUTTON_ICON);
		locationButton.setRequestLocation(true);
		
		KeyboardRow row = new KeyboardRow();
		row.add(locationButton);
		
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		keyboard.setKeyboard(Collections.singletonList(row));
		keyboard.setResizeKeyboard(true);
		keyboard.setOneTimeKeyboard(true);
		
		return keyboard;
	}
	
	public ReplyKeyboardMarkup languageRequestKeyboard() {
		
		KeyboardButton ruButton = new KeyboardButton(StaticStrings.RU);
		KeyboardButton enButton = new KeyboardButton(StaticStrings.EN);
		KeyboardButton uaButton = new KeyboardButton(StaticStrings.UA);
		
		KeyboardRow row = new KeyboardRow();
		row.add(ruButton);
		row.add(enButton);
		row.add(uaButton);
		
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		keyboard.setKeyboard(Collections.singletonList(row));
		keyboard.setResizeKeyboard(true);
		keyboard.setOneTimeKeyboard(true);
		
		return keyboard;
	}
}
