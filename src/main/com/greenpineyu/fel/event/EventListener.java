package com.greenpineyu.fel.event;

public interface EventListener<T extends Event> {
	
	public String getId();
	
	/**
	 * 当发生某个事件时，触发的动作
	 * @param event
	 * @return
	 */
	public Object onEvent(T event);

}
