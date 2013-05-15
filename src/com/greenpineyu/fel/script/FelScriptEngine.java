package com.greenpineyu.fel.script;
import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.greenpineyu.fel.Fel;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.FelContext;

public class FelScriptEngine extends AbstractScriptEngine implements Compilable {

	private final FelEngine engine = Fel.newEngine();

	private final ScriptEngineFactory factory;

	public FelScriptEngine(ScriptEngineFactory factory) {
		this.factory = factory;
	}

	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return engine.eval(script, toFelContext(context));
	}

	public FelContext toFelContext(ScriptContext context) {
		return new ContextAdaptor(context);
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return null;
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return factory;
	}

	@Override
	public CompiledScript compile(String script) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompiledScript compile(Reader script) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

}
