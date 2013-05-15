package com.greenpineyu.fel;

import java.util.Map;

import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.context.Var;
import com.greenpineyu.fel.parser.FelNode;

public class Fel {
	static FelEngineImpl engine = new FelEngineImpl();

	public static Object eval(String exp, Var... vars) {
		return engine.eval(exp, vars);
	}

	public static Object eval(String exp, Map<String, Object> vars) {
		FelContext ctx = new MapContext(vars);
		return engine.eval(exp, ctx);
	}

	public static Expression compile(String exp, Var... vars) {
		return engine.compile(exp, vars);
	}

	public static Expression compile(String exp, Map<String, Object> vars) {
		return engine.compile(exp, new MapContext(vars));
	}

	public static FelEngine newEngine() {
		return new FelEngineImpl();
	}

	public static FelContext newContext(String name) {
		if ("Array".equalsIgnoreCase(name)) {
			return new ArrayCtxImpl();
		}
		return new MapContext();
	}

	public static void main(String[] args) {
		String exp = "1+100000000000";
		FelNode node = engine.parse(exp);
		FelContext ctx = engine.getContext();
		Class<?> returnType = node.toMethod(ctx).returnType(ctx, node);
		System.out.println(returnType);
	}

}

