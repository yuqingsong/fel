package com.greenpineyu.fel.function.operator.big;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.compile.InterpreterSourceBuilder;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.StableFunction;
import com.greenpineyu.fel.function.TolerantFunction;
import com.greenpineyu.fel.parser.FelNode;

public class BigAdd extends StableFunction {

	/*
	 * 大数值加法运算（并保证精度）
	 * 
	 * @see .script.function.Function#call(.script.AstNode,
	 * .script.context.ScriptContext)
	 */
	@Override
	public Object call(FelNode node, FelContext context) {
		List<FelNode> children = node.getChildren();
		if (children == null || children.isEmpty()) {
			return null;
		}
		Object left = TolerantFunction.eval(context, children.get(0));
		if (children.size() == 1) {
			return left;
		}
		Object right = TolerantFunction.eval(context, children.get(1));

		if (left instanceof String || right instanceof String) {
			return ObjectUtils.toString(left).concat(ObjectUtils.toString(right));
		}

		try {
			// 浮点型，转换成BigDecimal
			if (hasFloat(left, right)) {
				BigDecimal l = NumberUtil.toBigDecimal(left);
				BigDecimal r = NumberUtil.toBigDecimal(right);
				return l.add(r);
			}

			// 数值弄，转换成BigInteger
			if (isInt(left, right)) {
				BigInteger l = NumberUtil.toBigInteger(left);
				BigInteger r = NumberUtil.toBigInteger(right);
				return l.add(r);
			}
		} catch (NumberFormatException e) {
			// 忽略
		}
		return ObjectUtils.toString(left).concat(ObjectUtils.toString(right));
	}

	public static boolean isInt(Object left, Object right) {
		return isInt(left) && isInt(right);
	}

	public static boolean hasFloat(Object left, Object right) {
		return isFloat(left) || isFloat(right);
	}

	@Override
	public String getName() {
		return "+";
	}

	public static boolean isFloat(Object o) {
		return NumberUtil.isFloatingPoint(o) || (o instanceof BigDecimal);
	}

	public static boolean isInt(Object o) {
		return NumberUtil.isNumberable(o) || (o instanceof BigInteger);
	}

	/*
	 * 由于java中的”+、-、*、/"等不支持BigInteger和BigDecimal，所以生成的代码效率不高。
	 * @see com.greenpineyu.fel.function.Function#toMethod(com.greenpineyu.fel.parser.FelNode, com.greenpineyu.fel.context.FelContext)
	 */
	@Override
	public SourceBuilder toMethod(FelNode node, FelContext ctx) {
		return InterpreterSourceBuilder.getInstance();
	}

	public static void main(String[] args) {
		BigInteger left = new BigInteger("1000000000000000000");
		calc(left);
		// System.out.println(Math.random());
		// calc(Double.MAX_VALUE);
	}

	/**
	 * @param left
	 */
	/*	private static void calc(Double left) {
			FelEngine engine = FelBuilder.bigNumberEngine();
			BigDecimal result = new BigDecimal(left);
			engine.getContext().set("a", result);
			Object value = engine.eval("a+a");
			System.out.println("excep:" + result.add(result));
			System.out.println("equals:" + value.equals(result.add(result)) + ";value:" + value);
		}*/

	/**
	 * @param left
	 */
	private static void calc(BigInteger left) {
		// String exp = left.toString() + "+" + left.toString();
		// Object value = FelBuilder.highAccuracyEngine().eval(exp);

		// System.out.println("equals:" + value.equals(left.add(left)) +
		// ";value:" + value);
		String exp2 = "(-9484950.4f)+(-188132624.1)-(-10645.84)*(-196528.43)/(-109190992.2)+(-12353902.38)+(-502721396.8)";
		String exp3 = "(-822091040.15)==(0)+(0)+(-9484950.4f)+(-188132624.1)+(-10645.84)+(-196528.43)+(-109190992.2)+(-12353902.38)+(0)+(0)+(-502721396.8)";
		exp2 = "(0)+(0)+(-9484950.4f)+(-188132624.1)+(-10645.84)+(-196528.43)+(-109190992.2)+(-12353902.38)+(0)+(0)+(-502721396.8)";
		// Object r = FelEngine.instance
		// .eval(exp2);
		// exp2 =
		// "(-1111000000000000000000.123456789)*(-1000000000000000000000000000123123123123.123456789)";
		FelEngine eng = FelBuilder.bigNumberEngine();
		Object value = eng.eval("1.666+2323.7777");
		System.out.println("1.666+2323.7777:" + value);
		eng.getContext().set("a", new BigDecimal("1234567891000000123123123123.123456"));
		System.out.println("right:" + eng.eval(exp2));
		System.out.println("compile:"
 + eng.compile("a*a", eng.getContext()).eval(eng.getContext()));
		// System.out.println("fel0.8:" + FelEngine.instance.eval(exp2));
		System.out.println("equals:" + eng.eval(exp3));
		// System.out
		// .println("old:" + r);
		System.out.print("java:");
		System.out.println((-9484950.4f) + (-188132624.1) + (-10645.84) + (-196528.43) + (-109190992.2)
				+ (-12353902.38) + (0) + (0) + (-502721396.8));
	}

}
