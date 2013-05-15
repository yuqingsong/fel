package com.greenpineyu.fel.script;

import javax.script.ScriptContext;

import com.greenpineyu.fel.context.AbstractContext;

public class ContextAdaptor extends AbstractContext {

	private final ScriptContext ctx;

	public ContextAdaptor(ScriptContext context) {
		ctx = context;
	}

	@Override
	public Object get(String name) {
		return ctx.getAttribute(name);
	}

}
