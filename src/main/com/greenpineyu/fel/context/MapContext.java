package com.greenpineyu.fel.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 将变量保存在map中，此类是否线程安全取决于构建方法中的map参数是否线程安全
 * @author yuqingsong
 * 
 */
/*
 * 修改此类里时请遵守“此类是否线程安全取决于构建方法中的map参数是否线程安全”规则
 */
public class MapContext implements FelContext {
	private Map<String, Var> context;
	public MapContext() {
		this(null);
	}
	
	public MapContext(Map<String,Object> map){
		if(map != null){
			if (map instanceof ConcurrentMap) {
				this.context = new ConcurrentHashMap<String, Var>(map.size());
			} else {
				this.context = new HashMap<String, Var>(map.size());
			}
			for (Map.Entry<String, Object> e : map.entrySet()) {
				String name = e.getKey();
				Object value = e.getValue();
				this.set(name, value);
			}
		} else {
			this.context = new HashMap<String, Var>();
		}
	}

	@Override
	public Object get(String name) {
		return getVar(name).getValue();
	}



	@Override
	public void set(String name, Object value) {
		this.context.put(name, new Var(name, value));
	}

	public static String toString(Object var) {
		return var == null ? null : var.toString();
	}

	@Override
	public Var getVar(String name) {
		Var var = innerGet(name);
		if(var == null){
			return NOT_FOUND;
		}
		return var;
	}

	private Var innerGet(String name) {
		return context.get(name);
	}

	@Override
	public void setVar(Var var) {
		context.put(var.getName(), var);
	}

}


