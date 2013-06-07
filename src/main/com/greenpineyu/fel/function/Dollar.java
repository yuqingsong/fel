package com.greenpineyu.fel.function;

import com.greenpineyu.fel.Fel;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.compile.FelMethod;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.parser.FelNode;

/**
 * $函数，通过$获取class或者创建对象 ${Math} 结果为 Math.class ${Dollar.new} 结果为 new Dollar()
 * 
 * @author yuqingsong
 * 
 */
public class Dollar implements Function {


	@Override
	public String getName() {
		return "$";
	}

	@Override
	public Object call(FelNode node, FelContext context) {
		String txt = getChildValue(node,context);

		boolean isNew = isNew(txt);
		Class<?> cls = getClass(txt, isNew);
		if (isNew) {
			return newObject(cls);

		} else {
			return cls;
		}
	}

	private Object newObject(Class<?> cls) {
		Object o = null;
		if (cls != null) {
			try {
				o = cls.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return o;
	}

	private static final String suffix = ".new";

	private Class<?> getClass(String txt, boolean isNew) {
		String className = txt;
		if (isNew) {
			className = className.substring(0, txt.length() - suffix.length());
		}
		if (className.indexOf(".") == -1) {
			className = "java.lang." + className;
		}
		try {
			Class<?> clz = Class.forName(className);
			return clz;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isNew(String txt) {
		boolean isNew = txt.endsWith(suffix);
		return isNew;
	}

	private String getChildValue(FelNode node,FelContext ctx) {
		if(node.getChildCount()>0){
			FelNode child = (FelNode) node.getChild(0);
			return ObjectUtils.toString(TolerantFunction.eval(ctx, child));
		}
		return null;
	}
	private String getChildText(FelNode node,FelContext ctx) {
		if(node.getChildCount()>0){
			FelNode child = (FelNode) node.getChild(0);
			SourceBuilder sb = child.toMethod(ctx);
			Class<?> returnType = sb.returnType(ctx, child);
			String childCode = sb.source(ctx, child);
			if(!String.class.isAssignableFrom(returnType)){
			   childCode = "ObjectUtils.toString("+childCode+")";	
			}
			return childCode;
		}
		return "null";
	}

	@Override
	public SourceBuilder toMethod(FelNode node, FelContext ctx) {
		String txt = getChildText(node,ctx);

		if (txt != null) {
			txt = txt.trim();
			if (txt.startsWith("\"") && txt.endsWith("\"")) {
				txt = txt.substring(1, txt.length() - 1);
			}
		}
		boolean isNew = isNew(txt);
		Class<?> cls = getClass(txt, isNew);
		String code = cls.getName();
		if (isNew) {
			code = "new " + code + "()";
		}
		return new FelMethod(cls, code);
		/*
		Class<?> cls = getClass(txt,false);
		return new FelMethod(cls, cls.getName());
		*/
	}

	public static void main(String[] args) {
		// System.out.println("abc.new".endsWith(".new"));
		String exp = "$('Math').max($('Math').min(1,2),3).doubleValue()";
		// exp = "$('String.new').concat('abc')";
		exp = "'abc'.indexOf('bc')";
		exp = "-1";
		Object eval = Fel.compile(exp).eval((FelContext)null);
		System.out.println(eval);
	}

}
