package com.greenpineyu.fel.event;

import com.greenpineyu.fel.context.FelContext;


public class EventImpl implements Event {
	
	private String id;

	private String expression;

	private FelContext context;

	private final String message;

	public EventImpl(String id, String expression, FelContext context, String message) {
		this.id = id;
		this.expression = expression;
		this.message = message;
	}

	public FelContext getContext() {
		return context;
	}

	public void setContext(FelContext context) {
		this.context = context;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	public String getMessage() {
		return message;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
