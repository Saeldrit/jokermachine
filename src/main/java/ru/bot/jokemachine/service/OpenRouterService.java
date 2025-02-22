package ru.bot.jokemachine.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.bot.jokemachine.config.properties.OpenRouterProperties;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenRouterService {

	private final RestClient restClient;
	private final OpenRouterProperties openRouterProperties;

	private static final String PROMT = """
			Проанализируй историю сообщений пользователя из телеграмм.
			Напиши небольшую стендап прожарку (в среднем 3 предложения) используя сарказм и юмор.
			Используй его имя и пол, создай впечатление что вы друзья.
			Подчеркни слабости и нелепые ситуации.
			Используй мат в прожарке.
			Набор сообщений пользователя:
			'%s'
			""";

	public String getAiResponse(String userName, StringBuilder messages) {
		return restClient.post()
				.uri(openRouterProperties.api().url())
				.body(createRequest(userName, messages))
				.header("Authorization", "Bearer " + openRouterProperties.api().key())
				.header("Content-Type", "application/json")
				.retrieve()
				.onStatus(status -> !status.is2xxSuccessful(), (req, res) -> {
					throw new RuntimeException("Ошибка: " + res.getStatusCode());
				})
				.body(Response.class)
				.getChoices().get(0).getMessage().getContent();
	}

	private static Request createRequest(String userName, StringBuilder messages) {
		Request.Message message = new Request.Message();
		message.setRole("user");
		message.setContent(PROMT.formatted(userName, messages));

		Request request = new Request();
		request.setModel("deepseek/deepseek-chat:free");
		request.setMessages(List.of(message));
		request.setTemperature(0.7);

		return request;
	}

	@Data
	static class Request {
		private String model;
		private List<Message> messages;
		private double temperature;

		@Data
		public static class Message {
			private String role;
			private String content;
		}
	}

	@Data
	static class Response {

		private List<Choice> choices;

		@Data
		public static class Choice {
			private Message message;

			@Data
			public static class Message {
				private String content;
			}
		}
	}
}
