package com.greenpineyu.fel.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.greenpineyu.fel.common.ReflectUtil;
import com.greenpineyu.fel.compile.InterpreterSourceBuilder;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.AbstractContext;
import com.greenpineyu.fel.context.ArrayCtx;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.event.EventImpl;
import com.greenpineyu.fel.event.Events;
import com.greenpineyu.fel.function.operator.Dot;

public class VarAstNode extends AbstFelNode  {
	private final String text;

	public VarAstNode(Token token) {
		super(token);
		this.text = token.getText();
	}

	@Override
	public String getText() {
		return this.text;
	}
	
	@Override
	public Object interpret(FelContext context, FelNode node) {
		boolean isUndefined = false;
		Object var = null;
		if (context == null) {
			isUndefined = true;
		} else {
			var = context.get(text);
			isUndefined = var == FelContext.NOT_FOUND;
		}

		if(isUndefined){
			String exp = getTokenText();
			String msg = "Variable " + text + " is not defined in expression[line:" + this.getLine() + ";pos:"
					+ getCharPositionInLine()
					+ ":"
					+ this.getTokenStopIndex() + "]  "
					+ exp;
			EventImpl event = new EventImpl(Events.UNDEFINED_VARIABLE, exp, context, msg);
			return onEvent(event);
		}
		return var;
	}

	public static boolean isVar(FelNode n) {
		if (n == null) {
			return false;
		}
		boolean isVar = n instanceof VarAstNode;
		if (isVar) {
			if (n instanceof CommonTree) {
				CommonTree treeNode = (CommonTree) n;
				CommonTree p = treeNode.parent;
				if (p != null) {
					if (Dot.DOT.equals(p.getText())) {
						// 点运算符后的变量节点不是真正意义上的变量节点。
						isVar = p.getChildren().get(0) == n;
					}
				}

			}
		}
		return isVar;
	}

	{
		this.builder = new SourceBuilder() {
			
			@Override
			public String source(FelContext ctx, FelNode node) {
				if(!node.isDefaultInterpreter()){
					// 用户自定义解析器
					return InterpreterSourceBuilder.getInstance().source(ctx, node);
				}
				Class<?> type = returnType(ctx, node);
				if (type == FelContext.NOT_FOUND_TYPE) {
					type = Object.class;
//					return InterpreterSourceBuilder.getInstance().source(ctx, node);
				}
				String varName = node.getText();
				String getVarCode = "context.get(\""+varName+"\")";
				if (ctx instanceof ArrayCtx) {
					ArrayCtx c = (ArrayCtx) ctx;
					int index = c.getIndex(varName);
					if(index>-1){
						//包含此节点
						getVarCode = "((context instanceof ArrayCtx)?((ArrayCtx)context).get("
							+ index
							+ "):context.get(\""
							+ varName + "\"))";
					}
				}
					
				String code = getVarFullCode(type, getVarCode);
				return code;
			}

			@Override
			public Class<?> returnType(FelContext ctx, FelNode node) {
				if (ctx == null) {
					return FelContext.NOT_FOUND_TYPE;
				}
				Class<?> type = AbstractContext.getVarType(node.getText(),ctx);
				if(type == null){
				   type = InterpreterSourceBuilder.getInstance().returnType(ctx, node);
				}
				return type;
			}
		};
	}

	public static String getVarFullCode(Class<?> type, String getVarCode) {
		String typeName = type.getCanonicalName();
		boolean isNumber = Number.class.isAssignableFrom(type);
		String code = "";
		if (ReflectUtil.isPrimitiveOrWrapNumber(type)) {
			code = "((" + typeName + ")" + getVarCode + ")";
		} else if (isNumber) {
			// 当float转double时，会丢失精度
			code = "((" + typeName + ")" + getVarCode + ").doubleValue()";
		} else {
			code = "((" + typeName + ")" + getVarCode + ")";
		}
		return code;
	}
}
