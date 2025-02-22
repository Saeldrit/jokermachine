package ru.bot.jokemachine.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openrouter")
public record OpenRouterProperties(Api api) {
	public record Api(String url, String key) {
	}
}
