package com.greenpineyu.fel.context;

import java.util.Map;

/**
 * 基于map的只读context
 * @author yqs
 *
 */
public class ReadOnlyMapContext extends AbstractContext{
	/* 为确保此类的线程安全，请不要提供setMap方法*/
	private Map<String, Object> map;

	public ReadOnlyMapContext(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public Object get(String name) {
		return map.get(name);
	}
	
}
