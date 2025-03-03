package ru.bot.jokemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bot.jokemachine.command.FryAnotherPersonCommand;
import ru.bot.jokemachine.command.FryCommand;
import ru.bot.jokemachine.fabric.CommandFabric;
import ru.bot.jokemachine.service.CommandHandler;
import ru.bot.jokemachine.service.FryAnotherPersonService;
import ru.bot.jokemachine.service.ai.AiService;

import java.util.List;

@Configuration
public class FabricCommandConfig {

	@Bean
	public CommandFabric commandFabric(List<CommandHandler> commandHandlers) {
		return new CommandFabric(commandHandlers);
	}

	@Bean
	public FryCommand fryCommand(AiService aiService) {
		return new FryCommand(aiService);
	}

	@Bean
	public FryAnotherPersonCommand fryAnotherPersonCommand(FryAnotherPersonService service) {
		return new FryAnotherPersonCommand(service);
	}
}
