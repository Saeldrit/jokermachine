package ru.bot.jokemachine.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.util.Cleaner;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FryAnotherPersonService {

	private final MessageDao messageDao;
	private final OpenRouterService openRouterService;

	public String getOpinion(Message message) {
		String text = message.getText();
		Long chatId = message.getChat().getId();
		String firstName = message.getFrom().getFirstName();

		String nickName = getWithNickName(text);

		if (StringUtils.isNotEmpty(nickName)) {
			Pair<String, List<String>> messagesPair = messageDao.getMessagesPair(nickName, chatId);
			String nameAnotherUser = messagesPair.getKey();
			List<String> messages = messagesPair.getValue();

			StringBuilder stringBuilder = new StringBuilder();
			messages.forEach(stringBuilder::append);

			String aiResponse = openRouterService.getFryAnotherPerson(nameAnotherUser, firstName, stringBuilder);

			return Cleaner.cleanUp(aiResponse);
		}

		return "";
	}

	private String getWithNickName(String message) {
		String[] split = message.split(" ");

		if (split.length > 1) {
			String nickName = split[1];

			final String nickNameStartSymbol = "@";

			if (nickName.startsWith(nickNameStartSymbol)) {
				return nickName.split(nickNameStartSymbol)[1];
			}
		}

		return null;
	}
}
