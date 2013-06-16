package com.greenpineyu.fel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;

/**
 * 提供简单的执行表达式接口
 * @author yqs
 * 
 */
public class Fel {

	private static FelEngine engine = newEngine();

	/**
	 * 执行表达式
	 * @param exp 表达式
	 * @return
	 */
	public static Object eval(String exp) {
		return engine.eval(exp);
	}

	/**
	 * 执行表达式
	 * @param exp 表达式
	 * @param vars 变量Map
	 * @return
	 */
	public static Object eval(String exp, Map<String, Object> vars) {
		return engine.eval(exp, vars);
	}

	/**
	 * 执行表达式
	 * @param exp 表达式
	 * @param context 引擎上下文
	 * @return
	 */
	public static Object eval(String exp, FelContext context) {
		return engine.eval(exp, context);
	}

	/**
	 * 编译表达式
	 * @param exp 表达式
	 * @param vars 变量Map
	 * @return
	 */
	public static Expression compile(String exp, Map<String, Object> vars) {
		return engine.compile(exp, vars);
	}

	/**
	 * 编译表达式
	 * @param exp 表达式
	 * @param context 引擎上下文
	 * @return
	 */
	public static Expression compile(String exp, FelContext context) {
		return engine.compile(exp, context);
	}
	/**
	 * 编译表达式
	 * @param exp 表达式
	 * @param context 引擎上下文
	 * @return
	 */
	public static Expression compile(String exp) {
		return compile(exp, (FelContext)null);
	}

	/**
	 * 创建表达式引擎
	 * @return
	 */
	public static FelEngine newEngine() {
		return new FelEngineImpl();
	}
	
	/**
	 * 创建context
	 * @return
	 */
	public static FelContext newContext() {
		return new ArrayCtxImpl();
	}	

	/**
	 * 创建线程安全的context
	 * @return
	 */
	public static FelContext newConcurrentMap() {
		return new MapContext(new ConcurrentHashMap<String, Object>());
	}

}

