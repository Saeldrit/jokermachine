package ru.bot.jokemachine.model;

import lombok.Builder;

@Builder
public record Parameter(Long idChat, String chatName, Person person, String message) {
}
