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

	public String getFryAnotherPerson(String nameAnotherUser, String orderUserName, StringBuilder messages) {
		String content = Promt.FRY_ANOTHER_USER.formatted(orderUserName, nameAnotherUser, messages);
		return getResponse(createRequest(content));
	}

	public String getFryMyself(String userName, StringBuilder messages) {
		String content = Promt.FRY_MYSELF.formatted(userName, messages);
		return getResponse(createRequest(content));
	}

	public String getDestinyMyself(String userName, StringBuilder messages) {
		String content = Promt.DESTINY_MYSELF.formatted(userName, messages);
		return getResponse(createRequest(content));
	}

	private String getResponse(Request request) {
		return restClient.post()
				.uri(openRouterProperties.api().url())
				.body(request)
				.header("Authorization", "Bearer " + openRouterProperties.api().key())
				.header("Content-Type", "application/json")
				.retrieve()
				.onStatus(status -> !status.is2xxSuccessful(), (req, res) -> {
					throw new RuntimeException("Ошибка: " + res.getStatusCode());
				})
				.body(Response.class)
				.getChoices().get(0).getMessage().getContent();
	}

	private static Request createRequest(String content) {
		Request.Message message = new Request.Message();
		message.setRole("user");
		message.setContent(content);

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

	private static class Promt {

		private static final String FRY_MYSELF = """
				Проанализируй историю сообщений пользователя из телеграмм.
				Напиши небольшую стендап прожарку (в среднем 3 предложения) используя сарказм и юмор.
				Используй его имя '%s' и пол, создай впечатление что вы друзья.
				Подчеркни слабости и нелепые ситуации.
				Используй мат в прожарке.
				Набор сообщений пользователя:
				'%s'
				""";

		private static final String FRY_ANOTHER_USER = """
				Проанализируй историю сообщений пользователя из телеграмм.
				Напиши небольшую стендап прожарку (в среднем 3 предложения) используя сарказм и юмор.
				Учти, что эту прожарку заказл другой пользователь '%s'.
				Используй имя пользователя, которого нужно прожарить '%s' и пол.
				Подчеркни слабости и нелепые ситуации.
				Используй мат в прожарке.
				Создай впечатление что вы друзья.
				Набор сообщений пользователя:
				'%s'
				""";
		private static final String DESTINY_MYSELF = """
				Ты ведьма и ясновидящая в старом поколении. Ты знаешь все о человеческой судьбе.
				На основе имени пользователя '%s' и манере общения (в среднем 3 предложения),
				предскажи его судьбу на сегодняшний день и ближайшее будущее.
				Не используй знак зодиака.
				Построй ответ в загадочной манере свойственной предсказаниям.
				Набор сообщений пользователя:
				'%s'
				""";
	}
}
