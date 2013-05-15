package com.greenpineyu.fel.exception;

import com.greenpineyu.fel.context.FelContext;

public interface ExceptionHandler {
	
	Object onException(Exception exception, String exp, FelContext context);

}
