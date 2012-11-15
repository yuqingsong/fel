package com.greenpineyu.fel.examples;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.parser.AbstFelNode;
import com.greenpineyu.fel.parser.FelNode;
import com.greenpineyu.fel.parser.VarAstNode;

public class Test {
	static public int[] sort(int[] a) {
		for (int i = 1; i < a.length; i++) {
			int current = a[i];
			int j = i - 1;
			for (; j > -1 && current < a[j]; j--) {
				a[j + 1] = a[j];
			}
			a[j + 1] = current;
		}
		return a;
	}
	

	private static void sort() {
		int[] b = new int[10];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Random().nextInt(10);
		}

		int[] a = sort(b);
		System.out.println(Arrays.toString(a));
	}

	static {
		PrintStream os = new PrintStream(System.out) {
			@Override
			public void print(String b) {
				super.print(b);
			};
		};
		System.setOut(os);
		System.setErr(os);
	}
	public static void main(String[] args) {
		// System.out.println("abcd");
		// t();
		// FelEngine e = FelEngine.instance;
		// String exp = "(1+2)1";
		// e.getParser().verify(exp);
		// FelNode node = e.parse(exp);
		// System.out.println(node);
		// Object eval = Fel.eval(exp);
		// System.out.println(eval);
		testMatch();
	}

	private static void t() {
		String exp = "$a*3+b*5+c*4+$d*10";
		FelNode node = FelEngine.instance.parse(exp);
		List<FelNode> nodes = AbstFelNode.getNodes(node);
		List<FelNode> $expNodes = new ArrayList<FelNode>();
		for (FelNode n : nodes) {
			if (n.getText().equals("*")) {
				if (contains$Var(n)) {
					$expNodes.add(n);
				}
			}
		}
		System.out.println(node);
	}

	static public boolean contains$Var(FelNode n) {
		List<FelNode> children = n.getChildren();
		if (children != null) {
			for (FelNode c : children) {
				if (c instanceof VarAstNode) {
					if (c.getText().startsWith("$")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	static void testMatch() {
		System.out.println(Integer.class.getCanonicalName());
		match("math(int,int)", "*math(int,int)");

	}

	static public boolean match(String input, String regex) {
		char[] chars = regex.toCharArray();
		StringBuilder sb = new StringBuilder("^");
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
			case '.':
			case '$':
			case '(':
			case ')':
				sb.append('\\');
				sb.append(c);
				break;
			case '*':
				break;
			default:

				break;
			}
			if (c == '.') {
				sb.append("\\.");
			} else if (c == '*') {
				sb.append(".*");
			}else{
				sb.append(c);
			}
		}
		sb.append("$");
		System.out.println(regex + ":" + input);
		System.out.println(sb);
		Matcher matcher = Pattern.compile(sb.toString()).matcher(input);
		boolean finder = matcher.find();
		System.out.println(finder);
		Method[] declaredMethods = Map.Entry.class.getDeclaredMethods();
		for (Method m : declaredMethods) {
			System.out.println(m.getDeclaringClass().getCanonicalName());
			System.out.println(m.getName());
			Class<?>[] parameterTypes = m.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				System.out.println(".." + parameterTypes[i].getCanonicalName());
			}
//			System.out.println(Arrays.toString(parameterTypes));
		}
		return finder;
	}

}
