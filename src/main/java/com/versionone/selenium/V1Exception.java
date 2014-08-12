package com.versionone.selenium;

public class V1Exception extends Exception {
	
	private static final long serialVersionUID = 1L;

	public V1Exception() {
	}

	public V1Exception(String message) {
		super(message);
	}

	public V1Exception(Throwable cause) {
		super(cause);
	}

	public V1Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public V1Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
