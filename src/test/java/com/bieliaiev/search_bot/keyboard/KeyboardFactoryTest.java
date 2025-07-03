package com.bieliaiev.search_bot.keyboard;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

class KeyboardFactoryTest {

    @Test
    void locationRequestKeyboard_Called_ContainLocationButton() {
    	
        ReplyKeyboardMarkup keyboard = KeyboardFactory.locationRequestKeyboard();

        assertThat(keyboard).isNotNull();
        assertThat(keyboard.getKeyboard()).hasSize(1);
        assertThat(keyboard.getKeyboard().get(0)).hasSize(1);

        KeyboardButton button = keyboard.getKeyboard().get(0).get(0);
        assertThat(button.getText()).isEqualTo("Поделиться местоположением");
        assertThat(button.getRequestLocation()).isTrue();

        assertThat(keyboard.getResizeKeyboard()).isTrue();
        assertThat(keyboard.getOneTimeKeyboard()).isTrue();
    }

}
