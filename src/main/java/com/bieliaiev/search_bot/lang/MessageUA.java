package com.bieliaiev.search_bot.lang;

public enum MessageUA {

	ADRESS_NOT_INDICATED("Адреса не вказана."),
	ENTER_SEARCH_QUERY("Будь ласка, введіть що ви бажаєте знайти. Наприклад: взуття"),
	SHARE_LOCATION("Будь ласка, поділіться своїм місцем розташування."),
	UNABLE_TO_DETERMINE_LOCATION("Не вдалося визначити місце розташування."),
	WELCOME("Ласкаво просимо! Будь ласка, введіть, що ви хочете знайти. Наприклад: банкомат."),
	NOTHING_FOUND("Нічого не знайдено 😔"),
	BUTTON_TEXT("Надіслати локацію"),
	OPEN_ON_MAP("\">Показати на карті</a>\n"),
	REVIEWS(" відгуків)"),
	LANGUAGE_SELECTED("Чудово! Спілкуємося українською 😊\nВведіть що ви бажаєте знайти.\nНаприклад: взуття");
	
	private final String text;
	
	MessageUA(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
