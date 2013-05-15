package com.greenpineyu.fel.event;

import java.util.HashMap;
import java.util.Map;

import com.greenpineyu.fel.exception.FelException;

public class EventMgr {
	
	private final Map<String, EventListener<Event>> listeners = new HashMap<String, EventListener<Event>>();
	
	
	@SuppressWarnings("unchecked")
	public void addListener(EventListener<? extends Event> listener) {
		checkParam(listener);
		listeners.put(listener.getId(), (EventListener<Event>) listener);
	}

	private void checkParam(EventListener<? extends Event> listener) {
		if(listener == null){
			throw new IllegalArgumentException(" listener is null");
		}
	}
	
	public EventListener<Event> getListener(String id) {
		return listeners.get(id);
	}

	public void removeListener(EventListener<? extends Event> listener) {
		checkParam(listener);
		listeners.remove(listener.getId());
	}
	
	public Object onEvent(Event e) {
		EventListener<Event> listener = listeners.get(e.getId());
		if (listener == null) {
			if (e instanceof ExceptionEvent) {
				Exception exce = ((ExceptionEvent) e).getException();
				if (exce instanceof RuntimeException) {
					throw (RuntimeException) exce;
				}
				throw new FelException(exce.getMessage(), exce);
			}
			return null;
		}
		return listener.onEvent(e);
	}

}
