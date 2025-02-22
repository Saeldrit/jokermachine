package ru.bot.jokemachine.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bot.jokemachine.config.properties.TelegramProperties;
import ru.bot.jokemachine.service.MediumRareBotService;

@Configuration
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramConfig {

	@Bean
	public TelegramBotsApi telegramBotsApi(MediumRareBotService bot) throws TelegramApiException {
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(bot);
		return botsApi;
	}
}

