package ru.bot.jokemachine.service.ai;

import com.example.generated.tables.records.MessagesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Result;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.model.ChatMessage;
import ru.bot.jokemachine.service.OpenRouterService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

	private final MessageDao messageDao;
	private final OpenRouterService openRouterService;

	public String getOpinion(Message message) {
		User user = message.getFrom();
		Long userId = user.getId();
		Long chatId = message.getChat().getId();
		String firstName = user.getFirstName();

		Result<MessagesRecord> resultMessages = messageDao.getMessage(userId, chatId);
		List<ChatMessage> chatMessages = resultMessages.into(ChatMessage.class);

		StringBuilder stringBuilder = new StringBuilder();
		chatMessages.forEach(m -> stringBuilder.append(m.getMessageText()).append(". "));

		String aiResponse = openRouterService.getAiResponse(firstName, stringBuilder);

		return cleanUp(aiResponse);
	}

	private String cleanUp(String aiResponse) {
		if (aiResponse.startsWith("\"") || aiResponse.startsWith("«")) {
			aiResponse = aiResponse.substring(1);
		}
		if (aiResponse.endsWith("\"") || aiResponse.endsWith("»")) {
			aiResponse = aiResponse.substring(0, aiResponse.length() - 1);
		}
		return aiResponse;
	}
}
