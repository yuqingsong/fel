package com.greenpineyu.fel.event;

public interface Event {
	
	String getId();

	Object getSource();

	String getMessage();

}
