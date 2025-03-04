package ru.bot.jokemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bot.jokemachine.command.DestinyCommand;
import ru.bot.jokemachine.command.FryAnotherPersonCommand;
import ru.bot.jokemachine.command.FryCommand;
import ru.bot.jokemachine.fabric.CommandFabric;
import ru.bot.jokemachine.service.CommandHandler;
import ru.bot.jokemachine.service.FryAnotherPersonService;
import ru.bot.jokemachine.service.DestinyService;
import ru.bot.jokemachine.service.FryService;

import java.util.List;

@Configuration
public class FabricCommandConfig {

	@Bean
	public CommandFabric commandFabric(List<CommandHandler> commandHandlers) {
		return new CommandFabric(commandHandlers);
	}

	@Bean
	public FryCommand fryCommand(FryService fryService) {
		return new FryCommand(fryService);
	}

	@Bean
	public FryAnotherPersonCommand fryAnotherPersonCommand(FryAnotherPersonService service) {
		return new FryAnotherPersonCommand(service);
	}

	@Bean
	public DestinyCommand destinyCommand(DestinyService destinyService) {
		return new DestinyCommand(destinyService);
	}
}
