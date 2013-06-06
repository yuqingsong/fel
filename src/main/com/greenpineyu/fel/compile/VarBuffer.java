package com.greenpineyu.fel.compile;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public  class  VarBuffer {
	/**
	 * 保存生成的代码
	 * 数组中的内容为{var,varCode,varName}
	 */
	static private ThreadLocal<Stack<Object[]>> varCodes;
	/**
	 * 保存变量对象
	 */
	static private ThreadLocal<Map<String,Object>> vars;
	static {
		varCodes = new ThreadLocal<Stack<Object[]>>();
		vars = new ThreadLocal<Map<String, Object>>();
	}
	
	private static int  count;

	/**
	 * 获取当前线程中的变量代码(类的属性代码)stack
	 * 
	 * @return
	 */
	private static Stack<Object[]> getVarCodes(){
		Stack<Object[]> stack = varCodes.get();
		if(stack == null){
			stack = new Stack<Object[]>();
			varCodes.set(stack);
		}
		return stack;
	}

	/**
	 * 获取当前线程中的保存的对象Map。
	 * 
	 * @return
	 */
	private static Map<String, Object> getVars(){
		return getFromThread(vars);
	}

	private static Map<String, Object> getFromThread(
			ThreadLocal<Map<String, Object>> threadLocal) {
		Map<String, Object> map = threadLocal.get();
		if(map == null){
			map = new HashMap<String, Object>();
			threadLocal.set(map);
		}
		return map;
	}
	
	public static void clean(){
		varCodes.set(null);
		vars.set(null);
	}

	/**
	 * 将变量存入线程
	 * 
	 * @param attrCode
	 */
	static public String push(Object var){
		return push(var,var.getClass());
		
	}

	static public String push(Object var, Class<?> varType) {
		
		Stack<Object[]> varCodeInThread = getVarCodes();
		for (Object[] varInfo : varCodeInThread) {
			// 比较对象及对象类型
			if (var == varInfo[0] && varType == varInfo[2]) {
				//重得push的对象，直接返回原有变量名称
				return (String) varInfo[1];
			}
		}
		String varName = getVarName(varType);
		
		String type = varType.getName();
		String varId = UUID.randomUUID().toString();
		
		Map<String, Object> varMap = getVars();
		varMap.put(varId, var);
		
		String code = "private static final " + type + " " + varName
		+" = ("+type+")"+VarBuffer.class.getSimpleName()+".pop(\""+varId+"\");";
		Object[] varInfo = new Object[] {
				var, varName, varType, code };
		varCodeInThread.push(varInfo);
		return varName;
	}
	
	
	
	static private String getVarName(Class<?> cls){
		int c;
		synchronized (VarBuffer.class) {
			c = count++;
		}
		return cls.getSimpleName().toLowerCase()+"_"+c;
	}

	/**
	 * 从线程取出变量
	 * 
	 * @param attrCode
	 * @return
	 */
	public static String pop(){
		Stack<Object[]> stack = getVarCodes();
		if(stack.empty()){
			return null;
		}
		return (String) stack.pop()[3];
	}
	
	public static Object pop(String name){
		return	getVars().remove(name);
	}
	
	private static ThreadLocal<String> tl = new ThreadLocal<String>();
	public static void main(String[] args) {
	int i = 100;
	ExecutorService pool = Executors.newFixedThreadPool(i);
	for (int j = 0; j < i; j++) {
		pool.submit(new Runnable() {
			
			@Override
			public void run() {
				String name = Thread.currentThread().getName();
				System.out.println("*******************"+name+" start************************");
				tl.set(name);
				get(name);
				System.out.println("*******************"+name+" end************************");
				
			}

			private void get(String name) {
				String string = tl.get();
				System.out.println(name+":"+(name == string));
			}
		});
	}
	}
}
