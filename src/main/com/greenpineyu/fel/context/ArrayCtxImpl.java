package com.greenpineyu.fel.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现接口 ArrayCtx。此类不是线程安全的
 * @author yqs
 * @see ArrayCtx
 * 
 */
public class ArrayCtxImpl implements ArrayCtx{
	
	
	/**
	 * 保存对象类型的变量,包含数值类型的变量
	 */
	private Var[] vars = new Var[10];
	
	private final Map<String,Integer> indexMap = new HashMap<String,Integer>(); 
	
	public ArrayCtxImpl(){
	}
	public ArrayCtxImpl(Map<String,Object> vars){
		if(vars!=null){
			for (Map.Entry<String, Object> e : vars.entrySet()) {
				this.set(e.getKey(), e.getValue());
			}
		}
	}

	@Override
	public Object get(int i) {
		return vars[i].getValue();
	}

	@Override
	public int getIndex(String name) {
		Integer i = innerGetIndex(name);
		return i!=null?i:-1;
	}
	private Integer innerGetIndex(String name) {
		return indexMap.get(name);
	}


	@Override
	public Object get(String name) {
		return getVar(name).getValue();
	}

	@Override
	public Var getVar(String name) {
		int index = getIndex(name);
		if(index == -1){
			return FelContext.NOT_FOUND;
		}
		return vars[index];
	}
	
	/**
	 * 获取变量，如果变量不存在，就添加一个新的变量。
	 * @param name
	 * @return
	 */
	private Var getVarWithoutNull(String name){
		Integer index = innerGetIndex(name);
		Var var = null;
		if (index != null) {
			var = vars[index];
		}
		if(var == null){
			var = new Var(name,null);
			setVar(var);
		}
		return var;
	}


	
    /**
     * 确保vars.length>minCapacity
     * @param minCapacity
     */
	private void ensureCapacity(int minCapacity) {
    	int oldCapacity = vars.length;
    	if (minCapacity > oldCapacity) {
    	    int newCapacity = (oldCapacity * 3)/2 + 1;
        	    if (newCapacity < minCapacity)
    		newCapacity = minCapacity;
                // minCapacity is usually close to size, so this is a win:
                vars = Arrays.copyOf(vars, newCapacity);
    	}
    }

	@Override
	public void set(String name, Object value) {
		getVarWithoutNull(name).setValue(value);
	}
	
	@Override
	public void setVar(Var var) {
		int i = addToIndexMap(var.getName());
		if(i<vars.length){
		}else{
			ensureCapacity(i+1);
		}
		vars[i] = var;
	}
	
	
	private int addToIndexMap(String name){
			Integer i = innerGetIndex(name);
			if(i!=null){
				return i ;
			}
			int newIndex = indexMap.size();
			indexMap.put(name, newIndex);
			return newIndex;
	}
/*	
	public static void main(String[] args) {
//		int times = 10000*10000;
//		ac(times);
//		mc(times);
//		testAc();
		testAc2();
		
	}
	
	public static void testAc(){
		ArrayCtx ac = new ArrayCtxImpl();
		ac.set("a","aValue");
		long begin = System.currentTimeMillis();
		int times = 1000*1000;
		for(int i = 0;i<times ;i++){
			String name = "a"+i++;
			String value = name;
			ac.set(name,value);
			if(value!=ac.get(ac.getIndex(name))){
				System.out.println("error");
			}
			if(value!=ac.get(name)){
				System.out.println("error");
			}
			
		}
		long end = System.currentTimeMillis();
		System.out.println("array:"+(end-begin));
	}
	public static void testAc2(){
		FelEngine e = new FelEngineImpl();
		FelContext ctx = e.getContext();
		ctx.set("num",1);
		e.compile("+num", ctx).eval(ctx);
		
	}
	
	public static void ac(int times){
		ArrayCtx ac = new ArrayCtxImpl();
		ac.set("a","aValue");
		long begin = System.currentTimeMillis();
//		int index = ac.getIndex("a");
		for(int i = 0;i<times;i++){
//			ac.set("a","aValue");
//			ac.get(index);
			ac.get("a");
		}
		long end = System.currentTimeMillis();
		System.out.println("array:"+(end-begin));
	}
	public static void mc(int times){
		FelContext mc = new MapContext();
		mc.set("a","aValue");
		long begin = System.currentTimeMillis();
		for(int i = 0;i<times;i++){
//			mc.set("a","aValue");
			mc.get("a");
		}
		long end = System.currentTimeMillis();
		System.out.println("map:"+(end-begin));
	}
	*/
	
	

}
