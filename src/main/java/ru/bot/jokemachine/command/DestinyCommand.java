package ru.bot.jokemachine.command;

import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.jokemachine.service.CommandHandler;
import ru.bot.jokemachine.service.DestinyService;

@Service
@RequiredArgsConstructor
public class DestinyCommand implements CommandHandler {

	private final DestinyService destinyService;

	@Override
	public String command() {
		return "/destiny";
	}

	@Override
	public String doCommand(Message message) {
		return destinyService.getOpinion(message);
	}
}
