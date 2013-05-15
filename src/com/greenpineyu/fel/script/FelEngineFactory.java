package com.greenpineyu.fel.script;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import com.greenpineyu.fel.Version;

public class FelEngineFactory implements ScriptEngineFactory {

	private final Map<String, String> attrMap = new HashMap<String, String>();

	{
		attrMap.put(ScriptEngine.ENGINE, "fel");
		attrMap.put(ScriptEngine.ENGINE_VERSION, Version.VERSION);
		attrMap.put(ScriptEngine.LANGUAGE, "fast and easy evaluator for java");
		attrMap.put(ScriptEngine.LANGUAGE_VERSION, Version.VERSION);
	}

	@Override
	public String getEngineName() {
		return attr(ScriptEngine.ENGINE);
	}

	private String attr(String name) {
		return attrMap.get(name);
	}

	@Override
	public String getEngineVersion() {
		return attr(ScriptEngine.ENGINE_VERSION);
	}

	@Override
	public List<String> getExtensions() {
		return Arrays.asList("fel");
	}

	@Override
	public List<String> getMimeTypes() {
		return Arrays.asList("text/fel");
	}

	@Override
	public List<String> getNames() {
		return Arrays.asList("fel", "fast-el");
	}

	@Override
	public String getLanguageName() {
		return "fel";
	}

	@Override
	public String getLanguageVersion() {
		return attr(ScriptEngine.LANGUAGE_VERSION);
	}

	@Override
	public Object getParameter(String key) {
		return attr(key);
	}

	@Override
	public String getMethodCallSyntax(String obj, String method, String... args) {
		String ret = obj + "." + method + "(";
		int len = args.length;
		if (len == 0) {
			ret += ")";
			return ret;
		}

		for (int i = 0; i < len; i++) {
			ret += args[i];
			if (i != len - 1) {
				ret += ",";
			} else {
				ret += ")";
			}
		}
		return ret;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public String getProgram(String... statements) {
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new FelScriptEngine(this);
	}

	public static void main(String[] args) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("fel");
		ScriptEngineFactory fac = engine.getFactory();
		System.out.println(fac.getMethodCallSyntax("var", "print", "hello"));
		String os = fac.getOutputStatement("abcd");
		System.out.println(os);
		String program = fac.getProgram("abc", "def");
		System.out.println(program);
	}

}
