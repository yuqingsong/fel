package com.greenpineyu.fel.exception;

@SuppressWarnings("serial")
public class CompileException extends FelException {
	public CompileException(String msg) {
		super(msg);
	}

	public CompileException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
