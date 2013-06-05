package com.greenpineyu.fel.context;




public interface FelContext {

	/**
	 * 如果没有找到变量，返回此变量
	 */
	Var NOT_FOUND = Var.notFound();
	
	Null NULL = new Null();

	/**
	 * NOT_FOUND对应的变量类型
	 */
	Class<?> NOT_FOUND_TYPE = NOT_FOUND.getType();


	Object get(String name);

	/**
	 * 设置变量
	 * 
	 * @param name
	 *            变量名称
	 * @param value
	 *            变量值
	 */
	void set(String name, Object value);
	
	
	/**
	 * 获取变量值
	 * 
	 * @param name
	 *            变量名称
	 * @return 变量值，如果没有找到变量，返回FelContext.NOT_FOUND
	 */
	 Var getVar(String name);
	
	 void setVar(Var var);


}
