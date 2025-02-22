package ru.bot.jokemachine.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessage {
	private Long id;
	private Long chatId;
	private Long personId;
	private String messageText;
	private LocalDateTime createdAt;
}