package com.bieliaiev.search_bot.lang;

public enum MessageEN {

	ADRESS_NOT_INDICATED("Address not specified."),
	ENTER_SEARCH_QUERY("Please enter what you want to find. For example: bank"),
	SHARE_LOCATION("Please share your location."),
	UNABLE_TO_DETERMINE_LOCATION("Location could not be determined."),
	WELCOME("Welcome! Please enter what you want to find. For example: bank"),
	NOTHING_FOUND("Nothing found 😔"),
	BUTTON_TEXT("Share location"),
	OPEN_ON_MAP("\">Open on map</a>\n"),
	REVIEWS(" reviews)"),
	LANGUAGE_SELECTED("Awesome! We'll chat in English 😊\nEnter what you want to find.\nFor example: bank");
	
	private final String text;
	
	MessageEN(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}
