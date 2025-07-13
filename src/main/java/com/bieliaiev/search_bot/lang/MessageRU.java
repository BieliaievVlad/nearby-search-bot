package com.bieliaiev.search_bot.lang;

public enum MessageRU {

	ADRESS_NOT_INDICATED("Адрес не указан."),
	ENTER_SEARCH_QUERY("Пожалуйста, введите что вы хотите найти. Например: банк"),
	SHARE_LOCATION("Пожалуйста, поделитесь своим местоположением."),
	UNABLE_TO_DETERMINE_LOCATION("Не удалось определить ваше местоположение."),
	WELCOME("Добро пожаловать! Пожалуйста, введите что вы хотите найти. Например: банк."),
	NOTHING_FOUND("Ничего не найдено 😔"),
	BUTTON_TEXT("Поделиться местоположением"),
	OPEN_ON_MAP("\">Показать на карте</a>\n"),
	REVIEWS(" отзывов)"),
	LANGUAGE_SELECTED("Отлично! Общаемся на русском 😊\nВведите что вы хотите найти.\nНапример: банк");
	
	private final String text;
	
	MessageRU(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
