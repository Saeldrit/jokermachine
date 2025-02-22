package ru.bot.jokemachine.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OpenRouterRequest {
	private String model;
	private List<ChatMessage> messages;
	private double temperature;
}