package com.greenpineyu.fel.event;

import com.greenpineyu.fel.context.FelContext;

public interface Event {
	
	String getId();

	String getExpression();

	FelContext getContext();

	String getMessage();

}
