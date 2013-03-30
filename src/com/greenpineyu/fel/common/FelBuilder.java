package com.greenpineyu.fel.common;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.function.FunMgr;
import com.greenpineyu.fel.function.operator.big.BigAdd;
import com.greenpineyu.fel.function.operator.big.BigDiv;
import com.greenpineyu.fel.function.operator.big.BigGreaterThan;
import com.greenpineyu.fel.function.operator.big.BigGreaterThanEqual;
import com.greenpineyu.fel.function.operator.big.BigLessThan;
import com.greenpineyu.fel.function.operator.big.BigLessThanEqual;
import com.greenpineyu.fel.function.operator.big.BigMod;
import com.greenpineyu.fel.function.operator.big.BigMul;
import com.greenpineyu.fel.function.operator.big.BigSub;
import com.greenpineyu.fel.parser.AntlrParser;
import com.greenpineyu.fel.parser.NodeAdaptor;
import com.greenpineyu.fel.security.RegexSecurityMgr;
import com.greenpineyu.fel.security.SecurityMgr;

public class FelBuilder {

	/**
	 * 构建安全管理器
	 * @return
	 */
	public static SecurityMgr newSecurityMgr() {
		Set<String> disables = new HashSet<String>();
		disables.add(System.class.getCanonicalName() + ".*");
		disables.add(Runtime.class.getCanonicalName() + ".*");
		disables.add(Process.class.getCanonicalName() + ".*");
		disables.add(File.class.getCanonicalName() + ".*");
		disables.add("java.net.*");
		disables.add("com.greenpineyu.fel.compile.*");
		disables.add("com.greenpineyu.fel.security.*");
		return new RegexSecurityMgr(null, disables);
	}

	public static void main(String[] args) {
		System.out.println(System.class.getCanonicalName());
		System.out.println(Long.toBinaryString(0xFFFFFFFFl));
		System.out.println(Long.toBinaryString(Long.MAX_VALUE).length());
		System.out.println(Long.MAX_VALUE);
	}

	public static FelEngine bigNumberEngine() {
		return bigNumberEngine(100);
	}

	public static FelEngine engine() {
		return new FelEngineImpl();
	}

	public static FelEngine bigNumberEngine(int setPrecision) {
		FelEngine engine = new FelEngineImpl();
		FunMgr funMgr = engine.getFunMgr();
		engine.setParser(new AntlrParser(engine, new NodeAdaptor() {
			@Override
			protected Number newFloatNumber(String text) {
				char lastChar = text.charAt(text.length() - 1);
				if (lastChar == 'l' || lastChar == 'L' || lastChar == 'd' || lastChar == 'D' || lastChar == 'f'
						|| lastChar == 'F') {
					text = text.substring(0, text.length() - 1);
				}
				return new BigDecimal(text);
			}
		}));
		funMgr.add(new BigAdd());
		funMgr.add(new BigSub());
		funMgr.add(new BigMul());
		funMgr.add(new BigDiv(setPrecision));
		funMgr.add(new BigMod());
		funMgr.add(new BigGreaterThan());
		funMgr.add(new BigGreaterThanEqual());
		funMgr.add(new BigLessThan());
		funMgr.add(new BigLessThanEqual());
		return engine;
	}

}
