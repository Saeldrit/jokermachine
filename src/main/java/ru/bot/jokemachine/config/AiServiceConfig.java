package ru.bot.jokemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.service.OpenRouterService;
import ru.bot.jokemachine.service.ai.AiService;

@Configuration
public class AiServiceConfig {

	@Bean
	public AiService aiService(MessageDao messageDao, OpenRouterService openRouterService) {
		return new AiService(messageDao, openRouterService);
	}
}
