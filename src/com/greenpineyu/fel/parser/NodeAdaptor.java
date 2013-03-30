package com.greenpineyu.fel.parser;

import java.math.BigInteger;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;

public class NodeAdaptor extends CommonTreeAdaptor {
	@Override
	public Object create(Token token) {
		if (token == null) {
			return new AbstFelNode(token){

				@Override
				public SourceBuilder toMethod(FelContext ctx) {
					return null;
				}};
		}
//		System.out.println(token.getText());

		/*
		Dot
		LikeIn
		Logical
		Equals
		Relational
		Additive
		Multiplicative
		Identifier
		FloatingPointLiteral
		CharacterLiteral
		StringLiteral
		BooleanLiteral
		HexLiteral
		OctalLiteral
		DecimalLiteral
		*/
		Object returnMe = null;
		int type = token.getType();
		String text = token.getText();
		switch (type) {
			case FelParser.Identifier:
				if("null".equals(text)){
//					returnMe = AbstFelNode.NULL;
					returnMe = new ConstNode(token, null);
			} else {
				returnMe = new VarAstNode(token);
			}
				break;

		/* 函数、操作符 开始 */
		case FelParser.Dot://.
		case FelParser.Additive:// +、-
		case FelParser.Multiplicative:// *、/
		case FelParser.Equals:// ==、!=
		case FelParser.Relational:// >、<、>=、<=
		case FelParser.And:// AND
		case FelParser.Or:// OR
		case FelParser.Ques:
		case FelParser.Bracket:
		case FelParser.Not:
				returnMe = new FunNode(token);
				break;
		/* 函数、操作符 结束 */

			/* 常量开始 */
			case FelParser.DecimalLiteral:
			// 数字-10进制
			// returnMe = NumberUtil.parseNumber(new Long(text));
			returnMe = narrowBigInteger(text, 10);
				break;
			case FelParser.HexLiteral:
			// 数字-16进制
				String num = text;
				if(text.startsWith("0x")||text.startsWith("0X")){
					num = text.substring(2);
				}
			// returnMe = NumberUtil.parseNumber(Long.parseLong(num, 16));
			returnMe = narrowBigInteger(num, 16);
				break;
			case FelParser.OctalLiteral:
			// 数字-8进制
			// returnMe = NumberUtil.parseNumber(Long.parseLong(text, 8));
			returnMe = narrowBigInteger(text, 8);
				break;

			case FelParser.FloatingPointLiteral:
			// 浮点型
			// System.out.println("text  :" + text);
			// returnMe = new Double(text);
			// System.out.println("double:" + returnMe);
			returnMe = newFloatNumber(text);
			// returnMe = narrowBigDecimal(text);
			// System.out.println("BigDec:" + returnMe);
			// System.out.println();
				break;
			case FelParser.BooleanLiteral:
			// 布尔值
				returnMe = Boolean.valueOf(text);
				break;
			case FelParser.CharacterLiteral:
			case FelParser.StringLiteral:
			// 字符串
			// 出掉字符串两端的单引号和双引号
				returnMe = text.substring(1, text.length() - 1);
				break;
		/* 常量结束 */
			default:
				break;
		}
		if (returnMe == null) {
			// 不能正确解析
			return CommonTree.INVALID_NODE;
		}
		if (returnMe instanceof CommonTree) {
			return returnMe;
		}
		return new ConstNode(token, returnMe);
	}

	private static Number narrowBigInteger(String ext, int radix) {
		BigInteger value = new BigInteger(ext, radix);
		return NumberUtil.narrow(value);
	}

	// private static Number narrowBigDecimal(String ext) {
	// char lastChar = ext.charAt(ext.length() - 1);
	// if (lastChar == 'l' || lastChar == 'L' || lastChar == 'd' || lastChar ==
	// 'D' || lastChar == 'f'
	// || lastChar == 'F') {
	// ext = ext.substring(0, ext.length() - 1);
	// }
	// BigDecimal value = new BigDecimal(ext);
	// return NumberUtil.narrow(value);
	// }

	/**
	 * 创建浮点型对象
	 * @param text
	 * @return
	 */
	protected Number newFloatNumber(String text) {
		return new Double(text);
	}

}
