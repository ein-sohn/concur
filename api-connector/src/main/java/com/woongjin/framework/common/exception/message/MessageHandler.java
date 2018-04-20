package com.woongjin.framework.common.exception.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageHandler {

	public static Message handleExMessage(MessageSource messageSource, String messageKey, Object[] parameters) {
		return MessageHandler.handleExMessage(messageSource, messageKey, parameters, null);
	}

	public static Message handleExMessage(MessageSource messageSource, String messageKey, Object[] parameters, String defaultMessage) {
		return MessageHandler.handleExMessage(messageSource, messageKey, parameters, LocaleContextHolder.getLocale(), defaultMessage);
	}

	public static Message handleExMessage(MessageSource messageSource, String messageKey, Object[] parameters, Locale locale, String defaultMessage) {

		String userMessageKey = messageKey;
		String solutionKey = messageKey + ".solution";
		String reasonKey = messageKey + ".reason";

		String userMessage = messageSource.getMessage(userMessageKey, parameters, defaultMessage, locale);
		String solution = messageSource.getMessage(solutionKey, null, userMessage, locale);
		String reason = messageSource.getMessage(reasonKey, null, userMessage, locale);

		return new Message(messageKey, userMessage, solution, reason);
	}

	public static Message handleExMessage(String message, Object[] parameters) {
		String userMessage = message;
		if (parameters != null) {
			userMessage = MessageFormat.format(message, parameters);
		}

		return new Message(userMessage, userMessage, userMessage, userMessage);
	}
}
