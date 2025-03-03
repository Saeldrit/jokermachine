package ru.bot.jokemachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bot.jokemachine.dao.MessageDao;
import ru.bot.jokemachine.model.Parameter;
import ru.bot.jokemachine.model.Person;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageDao messageDao;

	public void saveMessage(Message message) {
		Chat chat = message.getChat();
		Long idChat = chat.getId();
		String title = chat.getTitle();

		User user = message.getFrom();
		Long id = user.getId();
		String userName = user.getUserName();
		String firstName = user.getFirstName();

		String text = message.getText() + ". ";

		Person person = Person.builder()
				.id(id)
				.firstName(firstName)
				.login(userName)
				.build();
		Parameter parameter = Parameter.builder()
				.idChat(idChat)
				.chatName(title)
				.message(text)
				.person(person)
				.build();

		messageDao.save(parameter);
	}
}
