package com.greenpineyu.fel.event;

import java.util.HashMap;
import java.util.Map;

public class EventMgr {
	
	private final Map<String,EventListener<Event>> listeners = new HashMap<String,EventListener<Event>> ();
	
	
	public void addListener(EventListener<Event> listener){
		checkParam(listener);
		listeners.put(listener.getId(),listener);
	}

	private void checkParam(EventListener<Event> listener) {
		if(listener == null){
			throw new IllegalArgumentException(" listener is null");
		}
	}
	
	public void removeListener(EventListener<Event> listener){
		checkParam(listener);
		listeners.remove(listener.getId());
	}
	
	public Object onEvent(Event e){
		EventListener<Event> listener = listeners.get(e.getId());
		if (listener == null) {
			return null;
		}
		return listener.onEvent(e);
	}

}
