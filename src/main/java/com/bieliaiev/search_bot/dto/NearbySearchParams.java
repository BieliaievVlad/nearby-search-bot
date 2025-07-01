package com.bieliaiev.search_bot.dto;

import org.telegram.telegrambots.meta.api.objects.Location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearbySearchParams {

	private Location location;
	
    @Builder.Default
    private boolean rankbyDistance = true;
	
	private String keyword;
	
	@Builder.Default
	private String language = "ru";
}