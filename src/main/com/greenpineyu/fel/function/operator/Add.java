package com.greenpineyu.fel.function.operator;

import static com.greenpineyu.fel.common.NumberUtil.toDouble;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.Fel;
import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.common.ReflectUtil;
import com.greenpineyu.fel.compile.FelMethod;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.compile.VarBuffer;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.ReadOnlyMapContext;
import com.greenpineyu.fel.exception.EvalException;
import com.greenpineyu.fel.function.StableFunction;
import com.greenpineyu.fel.function.TolerantFunction;
import com.greenpineyu.fel.parser.FelNode;

public class Add extends StableFunction  {


	//public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


	/*
	 * (non-Javadoc)
	 * 
	 * @see .script.function.Function#call(.script.AstNode,
	 * .script.context.ScriptContext)
	 */
	@Override
	public Object call(FelNode node, FelContext context) {
		Object returnMe = null;
		for (Iterator<FelNode> iterator = node.getChildren().iterator(); iterator
				.hasNext();) {
			Object child = iterator.next();
			child = TolerantFunction.eval(context, child);
			if (child instanceof String) {
				if (returnMe == null) {
					returnMe = child;
					continue;
				}
					returnMe = returnMe + (String) child;
			}
			if (child instanceof Number) {
				if (returnMe == null) {
					returnMe = child;
					continue;
				}
				Number value = (Number) child;
				if (returnMe instanceof Number) {
					Number r = (Number) returnMe;
					returnMe = toDouble(r) + toDouble(value);
				}else if(returnMe instanceof String){
					String r = (String) returnMe;
					returnMe=r+value;
				}
			}
		}
		if(returnMe instanceof Number){
			return NumberUtil.parseNumber(returnMe.toString());
		}
		return returnMe;
	}
	
	/**
	 * 请勿修改方法签名，动态生成的代码会用到此方法
	 * @param text
	 * @param param
	 * @return
	 */
	public Object addObject(FelNode node,Object param){
		if(param == null){
			throw new EvalException("执行["+getNodeText(node)+"]操作失败，参数["+param+"]不能为空。");
		}
		if(param instanceof Number){
			return param;
		}else{
			throw new EvalException("执行["+getNodeText(node)+"]操作失败，参数["+param+"]必须是数值型。");
		}
	}
	
	/**
	 * 请勿修改方法签名，动态生成的代码会用到此方法
	 * @param text
	 * @param left
	 * @param right
	 * @return
	 */
	public Object addObject(FelNode node,Object left,Object right){
		if(left == null||right==null){
			return ObjectUtils.toString(left)+ObjectUtils.toString(right);
		}
		Class<?> leftType = left.getClass();
		Class<?> rightType = right.getClass();
		if(ReflectUtil.isPrimitiveOrWrapNumber(leftType)
				&&ReflectUtil.isPrimitiveOrWrapNumber(rightType)){
			return NumberUtil.parseNumber(NumberUtil.toDouble((Number)left)+NumberUtil.toDouble((Number)right));
		}else{
			return ObjectUtils.toString(left)+ObjectUtils.toString(right);
		}
		
	}


	@Override
	public String getName() {
		return "+";
	}

	@Override
	public FelMethod toMethod(FelNode node, FelContext ctx) {
		
		Class<?> type = null;
		List<FelNode> children = node.getChildren();
		StringBuilder sb = new StringBuilder();
		if (children.size() == 2) {
			FelNode left = children.get(0);
			SourceBuilder lm = left.toMethod(ctx);
			Class<?> leftType = lm.returnType(ctx, left);
			
			FelNode right = children.get(1);
			SourceBuilder rm = right.toMethod(ctx);
			Class<?> rightType = rm.returnType(ctx, right);
				if(CharSequence.class.isAssignableFrom(leftType)){
					type = leftType;
				} else if (CharSequence.class.isAssignableFrom(rightType)) {
					type = rightType;
				}else if(ReflectUtil.isPrimitiveOrWrapNumber(leftType)
						&&ReflectUtil.isPrimitiveOrWrapNumber(rightType)){
					type = NumberUtil.arithmeticClass(leftType, rightType);
				}
			
				if(type != null){
					appendArg(sb, lm,ctx,left);
					sb.append("+");
					appendArg(sb, rm,ctx,right);
				}else{
					//参数类型未知
					addCallCode(node, sb,this,"addObject");
					sb.append(lm.source(ctx, left));
					sb.append(",");
					sb.append(rm.source(ctx, right));
					sb.append(")");
					type = Object.class;
				}
				
			
		} else if (children.size() == 1) {
			FelNode right = children.get(0);
			SourceBuilder rm = right.toMethod(ctx);
			Class<?> rightType = rm.returnType(ctx, right);
		    if(ReflectUtil.isPrimitiveOrWrapNumber(rightType)){
				appendArg(sb, rm,ctx,right);
				type = rightType;
			}else {
				addCallCode(node, sb,this,"addObject");
				sb.append(rm.source(ctx, right));
				sb.append(")");
				type=Number.class;
//				throw new CompileException("编译["+getNodeText(node)+"]失败，参数必须是数值型");
			}
		}
		
//		appendArg(sb, rm,ctx,right);
		FelMethod m = new FelMethod(type, sb.toString());
		return m;
	}

	public static void addCallCode(FelNode node, StringBuilder sb,Object obj,String methodName) {
//		sb.append("("+Object.class.getName()+")");
//		sb.append(VarBuffer.push(node,FelNode.class));
		sb.append(VarBuffer.push(obj));
//		String text = ConstNode.toJavaSrc(getNodeText(node));
		sb.append(".").append(methodName).append("(").append(VarBuffer.push(node,FelNode.class)).append(",");
	}
	
	public static String getNodeText(FelNode node){
		/*FelNode child = node;
		int startIndex = Integer.MAX_VALUE;
		int startLine =Integer.MAX_VALUE;
		int endIndex = Integer.MAX_VALUE;
		int endLine = Integer.MAX_VALUE;
		while(node!=null){
			int line = node.getLine();
			int charInLine = node.getCharPositionInLine();
			//设置起始行和起始位置
			if(line<startLine){
				startLine = line;
				startIndex = charInLine;
			}else if(line == startLine){
				if(charInLine<startLine){
					startLine = charInLine; 
				}
			}
			//设置结束行和结束位置
			
			startLine = line<startLine?line:startLine;
		}
		return null;
		*/String text = node.getText();
		String tokenText = node.getTokenText();
		int line = node.getLine();
		int charPositionInLine = node.getCharPositionInLine();
		return "expression:"+tokenText+"; function:"+text+"; line:"+line+";charIndex:"+charPositionInLine;
	}
	

	private void appendArg(StringBuilder sb, SourceBuilder argMethod,FelContext ctx,FelNode node) {
		Class<?> t = argMethod.returnType(ctx, node);
		sb.append("(");
		if (ReflectUtil.isPrimitiveOrWrapNumber(t)
				|| CharSequence.class.isAssignableFrom(t)||t==FelContext.NOT_FOUND_TYPE) {
			// 数值型和字符型时，直接添加
			sb.append(argMethod.source(ctx, node));
		} else {
			sb.append("ObjectUtils.toString(").append(argMethod.source(ctx, node))
					.append(")");
		}
		sb.append(")");
	}

	/**
	 * 加法
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	/*public static Object add(Object left, Object right){
		if(left == null || right == null){
			throw new NullPointerException("调用add()方法出错！,原因：当前参数为空");
		}
		try {
			if (left instanceof Object[]){
				left = NumberUtil.calArray(left);
			}
			if (right instanceof Object[]){
				right = NumberUtil.calArray(right);
			}
			
			if (NumberUtil.isFloatingPointNumber(left) || NumberUtil.isFloatingPointNumber(right)) {
				double l = NumberUtil.toDouble(left);
				double r = NumberUtil.toDouble(right);
				return new Double(l + r);
			}
			
			if(left instanceof BigInteger && right instanceof BigInteger){
				BigInteger l = NumberUtil.toBigInteger(left);
				BigInteger r = NumberUtil.toBigInteger(right);
				return l.add(r);
			}
			
			if(left instanceof BigDecimal || right instanceof BigDecimal){
				BigDecimal l = NumberUtil.toBigDecimal(left);
				BigDecimal r = NumberUtil.toBigDecimal(right);
				return l.add(r);
			}
			
			if (left instanceof String && right instanceof Date) {
				return left + Add.DATE_FORMAT.format((Date) right);
			} else if (left instanceof Date && right instanceof String) {
				return Add.DATE_FORMAT.format((Date) left) + right;
			}
	
			BigInteger l = NumberUtil.toBigInteger(left);
			BigInteger r = NumberUtil.toBigInteger(right);
			BigInteger result = l.add(r);
			return NumberUtil.narrowBigInteger(left, right, result);
		} catch (Exception e) {
			return ObjectUtils.toString(left).concat(ObjectUtils.toString(right));
		}
	}*/
	
	public static void main(String[] args) throws Exception{
		Map<String,Object> m = new HashMap<String, Object>();
		ReadOnlyMapContext ctx = new ReadOnlyMapContext(m);
//		Fel.eval("print(100+4+'\n')");
		String e = "print(a+b+'\\'+'\n..')";
		String method = "*";
		method = "/";
		method = "%";
		e = "print(-(a\n"+method+"b"+method+"100))";
		Expression exp = Fel.compile(e, ctx);
//		Expression exp2 = Fel.compile("print(+c+'\n..')", ctx);
		m.put("a",10);
		m.put("b",11);
		exp.eval(m);
//		exp2.eval(ctx);
//		Expression compile = Fel.compile("print('\n', context)",ctx);
//		compile.eval(ctx);
	}

}
