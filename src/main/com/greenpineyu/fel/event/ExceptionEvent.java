package com.greenpineyu.fel.event;

import com.greenpineyu.fel.context.FelContext;

public class ExceptionEvent extends EventImpl {

	public ExceptionEvent(String id, String expression, FelContext context, String message) {
		super(id, expression, context, message);
	}

	private Exception exception;

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}


}
