package com.bieliaiev.search_bot;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class SearchBotApplicationTest {

    @Autowired
    private ApplicationContext context;
	
	@Test
	void contextLoads() {		
		assertThat(context).isNotNull();
	}

}
