package ru.bot.jokemachine.service;

import com.example.generated.tables.records.MessagesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Result;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.model.ChatMessage;
import ru.bot.jokemachine.util.Cleaner;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinyService {

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
		chatMessages.forEach(m -> stringBuilder.append(m.getMessageText()));

		String aiResponse = openRouterService.getDestinyMyself(firstName, stringBuilder);

		return Cleaner.cleanUp(aiResponse);
	}
}
