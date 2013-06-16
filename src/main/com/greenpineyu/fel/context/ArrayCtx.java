package com.greenpineyu.fel.context;

/**
 * 在@see FelContext的基础上，提供了通过索引获取变量的方法。 <br/>
 * 在使用Fel默认的编译器编译代码时，编译后的类会通过 {@link #get(i)}方法获取变量。
 * @author yqs
 * @see FelConext
 * 
 */
public interface ArrayCtx extends FelContext {
	
	/**
	 * 根据索引获取变量值
	 * @param i
	 * @return
	 */
	Object get(int i);
	
	/**
	 * 获取变量的索引
	 * @param name
	 * @return
	 */
	int getIndex(String name);

}
