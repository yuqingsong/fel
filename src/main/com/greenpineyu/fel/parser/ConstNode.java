package com.greenpineyu.fel.parser;

import org.antlr.runtime.Token;

import com.greenpineyu.fel.common.ReflectUtil;
import com.greenpineyu.fel.compile.FelMethod;
import com.greenpineyu.fel.compile.InterpreterSourceBuilder;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.compile.VarBuffer;
import com.greenpineyu.fel.context.FelContext;

/**
 * 常量节点
 * 
 * @author yqs
 * 
 */
public class ConstNode extends AbstFelNode {

	private Object value;

	public ConstNode(Token token, Object value) {
		super(token);
		this.value = value;
	}

	public Object interpret(FelContext context, FelNode node) {
		return value;
	}

	public SourceBuilder toMethod(FelContext ctx) {
		if (this.builder != null) {
			return this.builder;
		}
		if (!this.isDefaultInterpreter()) {
			return InterpreterSourceBuilder.getInstance();
		}
		return new FelMethod(this.getValueType(), this.toJavaSrc(ctx));
	}

	public Class<?> getValueType() {
		Class<?> t = null;
		if (value == null) {
			t = FelContext.NULL.getClass();
		} else {
			t = value.getClass();
		}
		return t;
	}

	public String toJavaSrc(FelContext ctx) {
		if (value == null) {
			return "null";
		}
		if (value instanceof String) {
			
			String str = (String) value;
			StringBuilder sb = new StringBuilder();
			sb.append("\"");
			sb.append(toJavaSrc(str));
			sb.append("\"");
			return sb.toString();
//			return "\"" + value + "\"";
		}
		if (ReflectUtil.isPrimitiveOrWrapNumber(getValueType())) {
			return value.toString();
		}
		return VarBuffer.push(value);
	}

	public static String toJavaSrc(String str ) {
		StringBuilder sb = new StringBuilder();
		char[] array = str.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			
			//'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\'
			switch(c){
			case '\n':sb.append("\\n");break; 
			case '\t':sb.append("\\t");break; 
			case '\b':sb.append("\\b");break; 
			case '\f':sb.append("\\f");break; 
			case '\r':sb.append("\\r");break; 
			case '\"':sb.append("\\\"");break;
			case '\\':sb.append("\\\\");break;
			default : sb.append(c);
			}
		}
		return sb.toString();
	}
	

	public boolean stable() {
		return true;
	}

}
