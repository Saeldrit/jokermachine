package ru.bot.jokemachine.fabric;

import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import ru.bot.jokemachine.service.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandFabric {

	private final Map<String, CommandHandler> commands;

	public CommandFabric(List<CommandHandler> commandHandlers) {
		commands = commandHandlers.stream()
				.collect(Collectors.toMap(
						CommandHandler::command,
						handler -> handler
				));
	}

	public CommandHandler doCommand(String command) {
		return commands.get(command);
	}
}
