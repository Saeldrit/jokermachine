package ru.bot.jokemachine.model;

import lombok.Builder;

@Builder
public record Person(Long id, String firstName, String login) {
}
