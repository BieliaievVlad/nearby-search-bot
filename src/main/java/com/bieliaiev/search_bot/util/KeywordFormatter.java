package com.bieliaiev.search_bot.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class KeywordFormatter {

	public String prepareKeyword (String original) {

		String transliterated = transliterate(original);
		
		if (original.equals(transliterated)) {
			return original;
			
		} else {
			return original + "|" + transliterated;
		}
	}
	
	private String transliterate(String text) {
	    Map<Character, String> map = Map.ofEntries(
	        Map.entry('а', "a"), Map.entry('б', "b"), Map.entry('в', "v"),
	        Map.entry('г', "g"), Map.entry('ґ', "g"), Map.entry('д', "d"),
	        Map.entry('е', "e"), Map.entry('є', "ie"), Map.entry('ж', "zh"),
	        Map.entry('з', "z"), Map.entry('и', "y"), Map.entry('і', "i"),
	        Map.entry('ї', "i"), Map.entry('й', "i"), Map.entry('к', "k"),
	        Map.entry('л', "l"), Map.entry('м', "m"), Map.entry('н', "n"),
	        Map.entry('о', "o"), Map.entry('п', "p"), Map.entry('р', "r"),
	        Map.entry('с', "s"), Map.entry('т', "t"), Map.entry('у', "u"),
	        Map.entry('ф', "f"), Map.entry('х', "kh"), Map.entry('ц', "ts"),
	        Map.entry('ч', "ch"), Map.entry('ш', "sh"), Map.entry('щ', "shch"),
	        Map.entry('ю', "iu"), Map.entry('я', "ia"), Map.entry('ь', ""),
	        Map.entry('ъ', ""), Map.entry('э', "e"), Map.entry('ё', "e")
	    );

	    StringBuilder result = new StringBuilder();
	    for (char c : text.toLowerCase().toCharArray()) {
	        result.append(map.getOrDefault(c, String.valueOf(c)));
	    }
	    return result.toString();
	}
}