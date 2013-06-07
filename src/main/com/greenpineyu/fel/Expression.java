package com.greenpineyu.fel;

import java.util.Map;

import com.greenpineyu.fel.context.FelContext;

public interface Expression {
	/**
	 * 求表达式的值
	 * @param arguments
	 * @return
	 */
	Object eval(FelContext context);
	
	Object eval(Map<String,Object> context);
	
}


