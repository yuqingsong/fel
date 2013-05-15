package com.greenpineyu.fel.event;

public class EventImpl implements Event {
	
	private String id;

	private Object source;

	private String message;

	public EventImpl(String id, Object source, String message) {
		this.id = id;
		this.source = source;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
