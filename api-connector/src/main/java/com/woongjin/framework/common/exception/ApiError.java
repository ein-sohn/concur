package com.woongjin.framework.common.exception;

public class ApiError {
	
	private String error = null;
	
	public ApiError(String error){
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
