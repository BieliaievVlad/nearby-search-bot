package com.bieliaiev.search_bot.util;

import org.springframework.stereotype.Component;

import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;
import com.bieliaiev.search_bot.lang.MessageKey;
import com.bieliaiev.search_bot.lang.MessageProvider;
import com.bieliaiev.search_bot.lang.StaticStrings;
import com.bieliaiev.search_bot.service.AppService;

@Component
public class ResultFormatter {
	
	private final AppService service;
	private final MessageProvider provider;
	
	public ResultFormatter(AppService service, MessageProvider provider) {
		this.service = service;
		this.provider = provider;
	}

	public String buildPlacesMessage(PlacesResponse.Result place, PlaceDetailsResponse details, long chatId) {

		StringBuilder sb = new StringBuilder();

		sb.append(StaticStrings.ICON_PLACE).append(escapeHtml(place.getName())).append(StaticStrings.NEW_LINE);
		sb.append(StaticStrings.ICON_LOCATION).append(escapeHtml(place.getAddress())).append(StaticStrings.NEW_LINE);
		sb.append(StaticStrings.ICON_PHONE).append(escapeHtml(details.getResult().getPhone())).append(StaticStrings.NEW_LINE);
		sb.append(StaticStrings.ICON_LINK_HTML_PREFIX).append(details.getResult().getLocationUrl())
			.append(provider.prepareMessage(service.getLanguage(chatId), MessageKey.OPEN_ON_MAP));

		if (place.getRating() != null) {
			sb.append(StaticStrings.ICON_STAR).append(place.getRating());

			if (place.getUserRatingsTotal() != null) {
				sb.append(StaticStrings.OPEN_PARENTHESIS_WITH_SPACE)
					.append(escapeHtml(String.valueOf(place.getUserRatingsTotal())))
					.append(provider.prepareMessage(service.getLanguage(chatId), MessageKey.REVIEWS));
			}

			sb.append(StaticStrings.NEW_LINE);
		}

		sb.append(StaticStrings.NEW_LINE);

		return sb.toString();
	}
	
	private String escapeHtml(String text) {
	    if (text == null) return "-";
	    return text.replace("&", "&amp;")
	               .replace("<", "&lt;")
	               .replace(">", "&gt;");
	}
}
