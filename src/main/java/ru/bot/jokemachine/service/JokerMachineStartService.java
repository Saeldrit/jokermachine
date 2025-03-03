package ru.bot.jokemachine.service;

import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.jokemachine.config.properties.TelegramProperties;
import ru.bot.jokemachine.fabric.CommandFabric;

import java.time.Instant;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class JokerMachineStartService extends TelegramLongPollingBot {

	/*
	* command == 4, whitespace == 1, @ == 1, telegram nickname =< 32
	* */
	private static final int COMMAND_LIMIT = 38;

	private final String botName;
	private final Instant botStartTime;
	private final CommandFabric commandFabric;
	private final MessageService messageService;

	@Autowired
	public JokerMachineStartService(TelegramProperties telegramProperties,
									CommandFabric commandFabric,
									MessageService messageService) {
		super(telegramProperties.bot().token());
		this.botName = telegramProperties.bot().name();
		this.commandFabric = commandFabric;
		this.messageService = messageService;
		this.botStartTime = Instant.now();
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			Message message = update.getMessage();

			if (isMessageAfterRun(message)) {
				String command = message.getText();
				CommandHandler commandHandler = null;

				if (command.length() < COMMAND_LIMIT && command.startsWith("/")) {
					commandHandler = commandFabric.getCommand(command);
				}

				if (isNull(commandHandler)) {
					messageService.saveMessage(message);
					return;
				}

				String content = commandHandler.doCommand(message);
				send(message, content);
			}
		}
	}

	public void send(Message message, String content) {
		if (StringUtils.isEmpty(content)) {
			content = "Кажется, ты не достоин моего ответа";
		}
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText(content);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error("Error while sending message", e);
		}
	}

	private boolean isMessageAfterRun(Message message) {
		long messageTimeMillis = message.getDate() * 1000L;
		return !(messageTimeMillis < botStartTime.toEpochMilli());
	}
}