package com.woongjin.framework.common.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.woongjin.framework.common.exception.message.DetailMessageSource;
import com.woongjin.framework.common.exception.message.Message;
import com.woongjin.framework.common.exception.message.MessageHandler;

public abstract class BaseRuntimeException extends RuntimeException implements DetailMessageSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Message message = new Message();
	protected String messageKey = null;
	protected Object[] messageParameters = null;
	//protected Locale locale = LocaleContextHolder.getLocale();

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param defaultMessage
	 *            default message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, String defaultMessage,
			Throwable wrappedException) {
		super(wrappedException);
		this.messageKey = messageKey;
		this.messageParameters = messageParameters;
		this.message = MessageHandler.handleExMessage(messageSource,
				messageKey, messageParameters, defaultMessage);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param locale
	 *            the locale for language
	 * @param defaultMessage
	 *            default message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Locale locale, String defaultMessage,
			Throwable wrappedException) {
		super(wrappedException);
		this.messageKey = messageKey;
		this.messageParameters = messageParameters;
		this.message = MessageHandler.handleExMessage(messageSource,
				messageKey, messageParameters, locale, defaultMessage);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, LocaleContextHolder.getLocale(),
				null, wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param locale
	 *            the locale for language
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Locale locale,
			Throwable wrappedException) {
		this(messageSource, messageKey, messageParameters, locale, null,
				wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Throwable wrappedException) {
		this(messageSource, messageKey, null, LocaleContextHolder.getLocale(), null,
				wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Locale locale, Throwable wrappedException) {
		this(messageSource, messageKey, null, locale, null, wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param locale
	 *            the locale for language
	 * @param defaultMessage
	 *            default message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			String defaultMessage, Throwable wrappedException) {
		this(messageSource, messageKey, null, LocaleContextHolder.getLocale(),
				defaultMessage, wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param defaultMessage
	 *            default message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Locale locale, String defaultMessage, Throwable wrappedException) {
		this(messageSource, messageKey, null, locale, defaultMessage,
				wrappedException);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters) {
		this(messageSource, messageKey, messageParameters, LocaleContextHolder.getLocale(),
				null, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param locale
	 *            the locale for language
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Locale locale) {
		this(messageSource, messageKey, messageParameters, locale, null, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param defaultMessage
	 *            default message
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, String defaultMessage) {
		this(messageSource, messageKey, messageParameters, LocaleContextHolder.getLocale(),
				defaultMessage, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param defaultMessage
	 *            default message
	 * @param locale
	 *            the locale for language
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Object[] messageParameters, Locale locale, String defaultMessage) {
		this(messageSource, messageKey, messageParameters, locale,
				defaultMessage, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey) {
		this(messageSource, messageKey, null, LocaleContextHolder.getLocale(), null, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param locale
	 *            the locale for language
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Locale locale) {
		this(messageSource, messageKey, null, locale, null, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param defaultMessage
	 *            default message
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			String defaultMessage) {
		this(messageSource, messageKey, null, LocaleContextHolder.getLocale(),
				defaultMessage, null);
	}

	/**
	 * The constructor with a message key, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param messageSource
	 *            message management service to extract a message
	 * @param messageKey
	 *            the message key of this exception
	 * @param locale
	 *            the locale for language
	 * @param defaultMessage
	 *            default message
	 */
	public BaseRuntimeException(MessageSource messageSource, String messageKey,
			Locale locale, String defaultMessage) {
		this(messageSource, messageKey, null, locale, defaultMessage, null);
	}

	/**
	 * The constructor with a message, with parameters, and with a wrapped
	 * exception (with all the formal parameters).
	 * 
	 * @param message
	 *            the message of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 * @param wrappedException
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(String message, Object[] messageParameters,
			Throwable wrappedException) {
		super(wrappedException);
		this.messageParameters = messageParameters;
		this.message = MessageHandler.handleExMessage(message,
				messageParameters);
	}

	/**
	 * The constructor with a message and parameters. No Throwable or Exception
	 * is transfered.
	 * 
	 * @param message
	 *            the message of this exception
	 * @param messageParameters
	 *            the parameters to substitute in the message
	 */
	public BaseRuntimeException(String message, Object[] messageParameters) {
		this(message, messageParameters, null);
	}

	/**
	 * default constructor
	 */
	public BaseRuntimeException() {
		this("BaseRTException without message", null, null);
	}

	/**
	 * Constructor with a message.
	 * 
	 * @param message
	 *            the message of this exception
	 */
	public BaseRuntimeException(String message) {
		this(message, null, null);
	}

	/**
	 * Constructor with a message and an exception.
	 * 
	 * @param message
	 *            the message of this exception
	 * @param exception
	 *            the exception that is wrapped in this exception
	 */
	public BaseRuntimeException(String message, Throwable exception) {
		this(message, null, exception);
	}

	/**
	 * Get the normal message structure for the exception.
	 * 
	 * @return the message structure for the user
	 */
	public Message getMessages() {
		return message;
	}

	/**
	 * Get the user message for the exception.
	 * 
	 * @return the user message
	 */
	public String getMessage() {
		return message.getUserMessage();
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Object[] getMessageParameters() {
		return messageParameters;
	}

	public void setMessageParameters(Object[] messageParameters) {
		this.messageParameters = messageParameters;
	}
}
