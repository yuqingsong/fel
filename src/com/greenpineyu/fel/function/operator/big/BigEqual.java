package com.greenpineyu.fel.function.operator.big;

import static com.greenpineyu.fel.function.operator.big.BigAdd.hasFloat;
import static com.greenpineyu.fel.function.operator.big.BigAdd.isInt;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.compile.InterpreterSourceBuilder;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.operator.Equal;
import com.greenpineyu.fel.parser.FelNode;

public class BigEqual extends Equal {


	@Override
	protected boolean compareNumber(Object left, Object right) {
		try {
			// 浮点型，转换成BigDecimal
			if (hasFloat(left, right)) {
				BigDecimal l = NumberUtil.toBigDecimal(left);
				BigDecimal r = NumberUtil.toBigDecimal(right);
				return l.equals(r);
			}

			// 数值弄，转换成BigInteger
			if (isInt(left, right)) {
				BigInteger l = NumberUtil.toBigInteger(left);
				BigInteger r = NumberUtil.toBigInteger(right);
				return l.equals(r);
			}
		} catch (NumberFormatException e) {
			// 忽略
		}
		return left.equals(right);
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
	}


}
