package com.greenpineyu.fel.examples;

import java.util.Map;
import java.util.regex.Pattern;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.ArrayCtx;
import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;

public class Test {
	public static void main(String[] args) {
		// t1();

//		t2();
		
		// t3();
		t4();
		t5();

	}
	
	private static void t4() {
		System.out.println(Pattern.matches("\\\\", "\\"));
		Object eval = FelEngine.instance.eval("$('java.util.regex.Pattern').matches('\\\\','\\')");
		System.out.println(eval);
	}
	private static void t3(){
		FelEngine fel = FelEngine.instance;
		// fel.addExceptionHandle(new ExceptionHandler() {
		//
		// @Override
		// public Object onException(Exception exception, String exp, FelContext
		// context) {
		// System.out.println("handle a Exception,the message is :" +
		// exception.getMessage());
		// return null;
		// }
		// });
		System.out.println(fel.eval("1+2+a/10"));
	}

	private static void t2() {
		FelEngine fel = new FelEngineImpl();
		FelContext ctx = fel.getContext();
		boolean isExp = FelEngine.instance.getParser().verify("abc");
		System.out.println(isExp);
		ctx.set("a", 5000);
		ctx.set("b", 1200);
		ctx.set("c", 750.0f);

		// Expression exp = fel.compile("(a*b)<(c*b)",ctx); //报类型不匹配的错误

		Expression exp = fel.compile("a-b>c", ctx);

		Object result1 = fel.eval("a*b<c-b");
		Object result2 = fel.eval("a-b>c");
		Object result3 = exp.eval(ctx);

		System.out.println(result1 + ":" + result2 + ":" + result3);
	}

	private static void t1() {
		FelEngine engine = FelEngine.instance;
		FelContext ctx = engine.getContext();
		ctx.set("a", 20);
		ctx.set("b", 50);
		Float f = 123.4f;
		ctx.set("c", f);
		String exp = "(a*b)>c";
		// exp = "(a*b)==c";
		Object r = engine.eval(exp);
		System.out.println(r);
		Object eval = engine.compile(exp, ctx).eval(ctx);
		System.out.println(eval);

		Integer i = 123456;
		Long l = 123456l;
		Integer j = 123456;
		Double d = 123.4d;
		ctx.set("d", d);
		System.out.println(engine.compile("c==d", ctx).eval(ctx));
		System.out.println("f>d:" + (f > d));
		System.out.println("f<d:" + (f < d));
	}

	Expression compiledExp;
	FelContext ctx;

	void init(String exp, Map<String, Object> context) {
		FelEngine engine = new FelEngineImpl();
		ctx = engine.getContext();
		for (String s : context.keySet()) {
			ctx.set(s, context.get(s));
		}
		compiledExp = engine.compile(exp, ctx);
	}

	Object evalExp() {
		return compiledExp.eval(ctx);
	}

	{
		/*final Map<String, Object> context = new HashMap<String, Object>();
		FelContext ctx = new AbstractContext() {
			@Override
			public Object get(String name) {
				return context.get(name);
			}
		};
		class MyCtx extends AbstractContext {

			private Map<String, Object> context = new HashMap<String, Object>();

			public void setContext(Map<String, Object> context) {
				this.context = context;
			}

			@Override
			public Object get(String name) {
				return context.get(name);
			}
		}*/
	}

	static class Exp implements Expression {

		@Override
		public Object eval(FelContext context) {
			System.out.println("FelContext");
			return null;
		}

		public Object eval(ArrayCtx context) {
			System.out.println("arrayctx");
			return null;
		}

	}

	public static void t5() {
		Expression exp = new Exp();
		exp.eval(new ArrayCtxImpl());
	}

}

