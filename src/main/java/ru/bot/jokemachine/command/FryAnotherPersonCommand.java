package ru.bot.jokemachine.command;

import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.jokemachine.service.CommandHandler;
import ru.bot.jokemachine.service.FryAnotherPersonService;

@Service
@RequiredArgsConstructor
public class FryAnotherPersonCommand implements CommandHandler {

	private final FryAnotherPersonService fryAnotherPersonService;

	@Override
	public String command() {
		return "/fry @";
	}

	@Override
	public String doCommand(Message message) {
		return fryAnotherPersonService.getOpinion(message);
	}
}
