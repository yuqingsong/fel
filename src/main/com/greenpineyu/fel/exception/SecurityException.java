package com.greenpineyu.fel.exception;

@SuppressWarnings("serial")
public class SecurityException extends FelException {
	public SecurityException(String msg) {
		super(msg);
	}

	public SecurityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
