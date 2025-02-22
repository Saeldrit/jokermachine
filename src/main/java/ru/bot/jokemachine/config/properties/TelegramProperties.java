package ru.bot.jokemachine.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramProperties(BotConfig bot) {
	public record BotConfig(String name, String token) {
	}
}
