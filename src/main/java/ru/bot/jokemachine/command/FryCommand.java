package ru.bot.jokemachine.command;

import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.jokemachine.service.CommandHandler;
import ru.bot.jokemachine.service.ai.AiService;

@Service
@RequiredArgsConstructor
public class FryCommand implements CommandHandler {

	private final AiService aiService;

	@Override
	public String command() {
		return "/fry";
	}

	@Override
	public String doCommand(Message message) {
		return aiService.getOpinion(message);
	}
}
