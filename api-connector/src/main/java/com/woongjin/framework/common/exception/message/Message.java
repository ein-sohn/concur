package com.woongjin.framework.common.exception.message;

import java.io.Serializable;

/**
 * 에러 메세지 정의 클래스
 * @author wjh
 *
 */
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 메세지 키
	 */
	private String messageKey = "";
	
	/**
	 * 사용자 정의 메세지
	 */
	private String userMessage = "";

	/**
	 * 에러 해결 메세지
	 */
	private String solution = "";	

	/**
	 * 에러 원인 메세지
	 */
	private String reason = "";

	public Message() {
	}
	
	public Message(String messageKey, String userMessage, String solution, String reason) {
		super();
		this.messageKey = messageKey;
		this.userMessage = userMessage;
		this.solution = solution;
		this.reason = reason;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}	

}
