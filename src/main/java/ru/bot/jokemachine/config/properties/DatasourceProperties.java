package ru.bot.jokemachine.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring")
public record DatasourceProperties(Datasource datasource) {
	public record Datasource(String password, String username, String url, String driverClassName) {

	}
}
