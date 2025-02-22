package ru.bot.jokemachine.config;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.bot.jokemachine.config.properties.OpenRouterProperties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenRouterProperties.class)
public class OpenRouterConfig {

	private final OpenRouterProperties properties;

	@Bean
	public RestClient restClient() {
		return RestClient.builder()
				.baseUrl(properties.api().url())
				.defaultHeader("Authorization", "Bearer " + properties.api().key())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}