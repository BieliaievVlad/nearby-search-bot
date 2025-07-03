package com.bieliaiev.search_bot.util;

import org.springframework.stereotype.Component;

import com.bieliaiev.search_bot.dto.PlaceDetailsResponse;
import com.bieliaiev.search_bot.dto.PlacesResponse;

@Component
public class ResultFormatter {

	public String buildPlacesMessage(PlacesResponse.Result place, PlaceDetailsResponse details) {

		StringBuilder sb = new StringBuilder();

		sb.append("🍣 ").append(escapeHtml(place.getName())).append("\n");
		sb.append("📍 ").append(escapeHtml(place.getAddress())).append("\n");
		sb.append("📞").append(escapeHtml(details.getResult().getPhone())).append("\n");
		sb.append("📌 <a href=\"").append(details.getResult().getLocationUrl()).append("\">Open on map</a>\n");

		if (place.getRating() != null) {
			sb.append("⭐ ").append(place.getRating());

			if (place.getUserRatingsTotal() != null) {
				sb.append(" (").append(escapeHtml(String.valueOf(place.getUserRatingsTotal()))).append(" отзывов)");
			}

			sb.append("\n");
		}

		sb.append("\n");

		return sb.toString();
	}
	
	private String escapeHtml(String text) {
	    if (text == null) return "-";
	    return text.replace("&", "&amp;")
	               .replace("<", "&lt;")
	               .replace(">", "&gt;");
	}
}
