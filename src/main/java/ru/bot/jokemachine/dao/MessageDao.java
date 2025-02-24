package ru.bot.jokemachine.dao;

import com.example.generated.tables.records.MessagesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.bot.jokemachine.model.Parameter;
import ru.bot.jokemachine.model.Person;

import java.util.Optional;

import static com.example.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class MessageDao {

	private static final int MAXIMUM = 50;
	private static final int LIMIT = 20;

	private final DSLContext dsl;

	@Transactional
	public void save(Parameter parameter) {
		Person person = parameter.person();
		Long chatId = parameter.idChat();
		Long personId = parameter.person().id();

		cleanUp(chatId, personId);

		if (!isPersonInChat(personId)) {
			createPerson(person);
		}

		if (!isChat(chatId)) {
			createChat(parameter);
		}
		if (!isUserInChat(chatId, personId)) {
			addUserToChat(parameter.idChat(), person.id());
		}
		sendMessage(parameter);
	}

	@Transactional
	public void saveContent(Long chatId, Long personId, String content) {
		dsl.insertInto(OLD_FIRES)
				.set(OLD_FIRES.CHAT_ID, chatId)
				.set(OLD_FIRES.PERSON_ID, personId)
				.set(OLD_FIRES.CONTENT, content)
				.onConflict(OLD_FIRES.CHAT_ID, OLD_FIRES.PERSON_ID)
				.doUpdate()
				.set(OLD_FIRES.CONTENT, content)
				.execute();
	}

	public Integer count(Long chatId, Long personId) {
		String sql = "SELECT COUNT(*) FROM chat.messages WHERE person_id = ? AND chat_id = ?";

		return dsl.resultQuery(sql, personId, chatId)
				.fetchInto(Integer.class).get(0);
	}

	public boolean isChat(Long chatId) {
		return dsl.fetchExists(
				dsl.selectFrom(CHATS)
						.where(CHATS.ID.eq(chatId))
		);
	}

	public boolean isUserInChat(Long chatId, Long personId) {
		return dsl.fetchExists(
				dsl.selectFrom(CHAT_USERS)
						.where(CHAT_USERS.CHAT_ID.eq(chatId))
						.and(CHAT_USERS.PERSON_ID.eq(personId))
		);
	}

	public boolean isPersonInChat(Long personId) {
		return dsl.fetchExists(
				dsl.selectFrom(PERSONS)
						.where(PERSONS.ID.eq(personId))
		);
	}

	public Result<MessagesRecord> getMessage(Long userId, Long chatId) {
		return dsl.selectFrom(MESSAGES)
				.where(MESSAGES.PERSON_ID.eq(userId))
				.and(MESSAGES.CHAT_ID.eq(chatId))
				.fetch();
	}

	public void createChat(Parameter parameter) {
		dsl.insertInto(CHATS)
				.set(CHATS.ID, parameter.idChat())
				.set(CHATS.CHAT_NAME, parameter.chatName())
				.execute();
	}

	public void addUserToChat(Long chatId, Long personId) {
		dsl.insertInto(CHAT_USERS)
				.set(CHAT_USERS.CHAT_ID, chatId)
				.set(CHAT_USERS.PERSON_ID, personId)
				.execute();
	}

	public void sendMessage(Parameter parameter) {
		dsl.insertInto(MESSAGES)
				.set(MESSAGES.CHAT_ID, parameter.idChat())
				.set(MESSAGES.PERSON_ID, parameter.person().id())
				.set(MESSAGES.MESSAGE_TEXT, parameter.message())
				.execute();
	}

	public void createPerson(Person person) {
		dsl.insertInto(PERSONS)
				.set(PERSONS.ID, person.id())
				.set(PERSONS.FIRST_NAME, person.firstName())
				.set(PERSONS.LOGIN, person.login())
				.execute();
	}

	public Optional<String> getOldContent(Long chatId, Long personId) {
		Result<Record1<String>> fetch = dsl.select(OLD_FIRES.CONTENT)
				.from(OLD_FIRES)
				.where(OLD_FIRES.CHAT_ID.eq(chatId))
				.and(OLD_FIRES.PERSON_ID.eq(personId))
				.fetch();

		if (fetch.isEmpty()) {
			return Optional.empty();
		}

		return Optional.ofNullable(fetch.get(0).get(OLD_FIRES.CONTENT));
	}

	private void cleanUp(Long chatId, Long personId) {
		if (count(chatId, personId) > MAXIMUM) {
			dsl.deleteFrom(MESSAGES)
					.where(MESSAGES.CHAT_ID.eq(chatId))
					.and(MESSAGES.PERSON_ID.eq(personId))
					.and(MESSAGES.ID.in(
							dsl.select(MESSAGES.ID)
									.from(MESSAGES)
									.where(MESSAGES.CHAT_ID.eq(chatId))
									.and(MESSAGES.PERSON_ID.eq(personId))
									.orderBy(MESSAGES.CREATED_AT.asc())
									.limit(LIMIT)
					))
					.execute();
		}
	}

}
