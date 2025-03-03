package ru.bot.jokemachine.util;

public class Cleaner {

	public static String cleanUp(String aiResponse) {
		if (aiResponse.startsWith("\"") || aiResponse.startsWith("«")) {
			aiResponse = aiResponse.substring(1);
		}
		if (aiResponse.endsWith("\"") || aiResponse.endsWith("»")) {
			aiResponse = aiResponse.substring(0, aiResponse.length() - 1);
		}
		return aiResponse;
	}
}
