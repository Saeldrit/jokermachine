package ru.bot.jokemachine.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {

	String command();

	String doCommand(Message message);
}
