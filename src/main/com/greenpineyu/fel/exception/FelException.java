package com.greenpineyu.fel.exception;

public class FelException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4806058909453938803L;

	public FelException(String msg) {
		super(msg);
	}

	public FelException(String msg, Throwable cause) {
		super(msg, cause);
	}

	static public String getCauseMessage(Exception e) {
		return "cause exception message:" + e.getMessage();
	}
}
