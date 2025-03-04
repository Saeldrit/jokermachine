package ru.bot.jokemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.service.FryAnotherPersonService;
import ru.bot.jokemachine.service.OpenRouterService;
import ru.bot.jokemachine.service.DestinyService;
import ru.bot.jokemachine.service.FryService;

@Configuration
public class ServiceConfig {

	@Bean
	public FryService aiService(MessageDao messageDao, OpenRouterService openRouterService) {
		return new FryService(messageDao, openRouterService);
	}

	@Bean
	public FryAnotherPersonService fryAnotherPersonService(MessageDao messageDao, OpenRouterService openRouterService) {
		return new FryAnotherPersonService(messageDao, openRouterService);
	}

	@Bean
	public DestinyService destinyService(MessageDao messageDao, OpenRouterService openRouterService) {
		return new DestinyService(messageDao, openRouterService);
	}
}
