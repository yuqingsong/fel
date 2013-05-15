package com.greenpineyu.fel.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.greenpineyu.fel.Expression;
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
		return eval(reader2String(reader), context);
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
		FelContext ctx = engine.getContext();
		final Expression compile = engine.compile(script, ctx);

		return new CompiledScript() {

			@Override
			public ScriptEngine getEngine() {
				return FelScriptEngine.this;
			}

			@Override
			public Object eval(ScriptContext context) throws ScriptException {
				return compile.eval(toFelContext(context));
			}
		};
	}

	@Override
	public CompiledScript compile(Reader script) throws ScriptException {
		return compile(reader2String(script));
	}

	private static String reader2String(Reader reader) {
		BufferedReader br = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("\r\n");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return sb.toString();
	}

}
