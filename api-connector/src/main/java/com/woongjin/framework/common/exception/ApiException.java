package com.woongjin.framework.common.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class ApiException extends BaseRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ApiException(String message) {
		super(message);
	}
	
	public ApiException(String message, Throwable exception) {
		super(message, exception);
	}
	
	public ApiException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Locale locale,
			Throwable wrappedException) {
		super(messageSource, messageKey, messageParameters, locale, wrappedException);
	}
	
}
