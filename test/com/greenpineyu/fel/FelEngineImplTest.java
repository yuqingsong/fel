package com.greenpineyu.fel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.greenpineyu.fel.common.ArrayUtils;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.Var;
import com.greenpineyu.fel.function.CommonFunction;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.interpreter.Interpreter;
import com.greenpineyu.fel.optimizer.Interpreters;
import com.greenpineyu.fel.parser.FelNode;

public class FelEngineImplTest {

	FelEngine engine = FelEngine.instance;
	FelEngine bigEngine = FelBuilder.bigNumberEngine();

	@DataProvider(name = "eval")
	public Object[][] evalData() {
		Object[][] a = new Object[1000][];
		AtomicInteger ai = new AtomicInteger(-1);
		getData(a, ai);
		add(a, ai, "1/3", 1.0 / 3);
		return subarray(a, ai.intValue());
	}

	@DataProvider(name = "bigEval")
	public Object[][] bigEvalData() {
		Object[][] a = new Object[1000][];
		AtomicInteger ai = new AtomicInteger(-1);
		getData(a, ai);
		// add(a, ai, "1/3", 1.0 / 3);
		int baseNum = 5;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			sb.append(baseNum);
		}
		String base = sb.toString();
		base = "55555555555555555555555555555555";
		addExp(a, ai, base, baseNum);
		addExp(a, ai, "55555555555555555555555555555555.55555555555555555555555555555555", baseNum);
		return subarray(a, ai.intValue());
	}

	private void getData(Object[][] a, AtomicInteger ai) {


		// 构建一个循环的foo
		Foo header = new Foo("header");
		Integer num = new Integer(5);
		Foo footer = new Foo("footer");
		Map<String, String> m = new HashMap<String, String>();

		Map<String, Object> varMap = getVarMap(header, num, footer, m);

		

		ArrayCtxImpl ctx = new ArrayCtxImpl(varMap);
		bigEngine.setContext(ctx);
		engine.setContext(new ArrayCtxImpl(varMap));
		FelContext jc = engine.getContext();
		jc.setVar(new Var("pc", m, Map.class));

		addNumberTest(header, num, footer, m, a, ai);

	}

	private void addExp(Object[][] a, AtomicInteger ai, String base, int baseNum) {
		int max = baseNum + 1;
		int min = baseNum - 1;
		String footString = base.substring(1);
		String headerMax = max + footString;
		String headerString = base.substring(0, base.length() - 1);
		String footMax = headerString + max;
		
		String headerMin = min + footString;
		String footMin = headerString + min;
		String[] numbers = new String[] {
				headerMax, footMax, headerMin, footMin, "1234", "1.12" };
		String[] operators = new String[] {
				">", "<", ">=", "<=", "==", "!=", "+", "-", "*", "/", "%" };
		for (int i = 0; i < operators.length; i++) {
			String oper = operators[i];
			for(int j = 0;j<20;j++){
				;
				Random random = new Random();
				String left = numbers[random.nextInt(10000) % numbers.length];
				String right = numbers[random.nextInt(10000) % numbers.length];
				add(a, ai, left + oper + right, getValue(left, right, oper));
			}
		}
	}

	public static Object getValue(String left, String right, String oper) {
		BigDecimal l = new BigDecimal(left);
		BigDecimal r = new BigDecimal(right);
		if (">".equals(oper)) {
			return l.compareTo(r) > 0;
		}
		if (">=".equals(oper)) {
			return l.compareTo(r) >= 0;
		}
		if ("<".equals(oper)) {
			return l.compareTo(r) < 0;
		}
		if ("<=".equals(oper)) {
			return l.compareTo(r) <= 0;
		}
		if ("==".equals(oper)) {
			return l.compareTo(r) == 0;
		}
		if ("!=".equals(oper)) {
			return l.compareTo(r) != 0;
		}
		if ("*".equals(oper)) {
			return l.multiply(r);
		}
		if ("/".equals(oper)) {
			if(left.indexOf(".")==-1&&right.indexOf(".")==-1){
				return new BigInteger(left).divide(new BigInteger(right));
			}
			return l.divide(r, new MathContext(100, RoundingMode.HALF_DOWN));
		}
		if ("+".equals(oper)) {
			return l.add(r);
		}
		if ("-".equals(oper)) {
			return l.subtract(r);
		}
		if ("%".equals(oper)) {
			return l.remainder(r);
		}

		throw new IllegalArgumentException();
	}

	private void addNumberTest(Foo header, Integer num, Foo footer, Map<String, String> m, Object[][] a,
			AtomicInteger ai) {
		add(a, ai, "f > d", Boolean.FALSE);
		add(a, ai, "f < d", Boolean.FALSE);

		// 8进制、16进制
		add(a, ai, "011", 011);
		add(a, ai, "021", 021);
		add(a, ai, "0x10abc", 0x10abc);
		add(a, ai, "0x1", 0x1);
		add(a, ai, "0xabc", 0xabc);
		add(a, ai, "0x10abc", 0x10abc);
		add(a, ai, "0xA+0XB+01", 0xA + 0XB + 01);
		// 算术运算
		add(a, ai, "+1", new Integer(1));
		add(a, ai, "+num", num);
		add(a, ai, "+num+num", +num + num);
		add(a, ai, "-num--num", -num - (-num));
		// if(true){
		// return subarray(object, i);
		// }
		add(a, ai, "-1", new Integer(-1));
		add(a, ai, "2*-1", -2);
		add(a, ai, "1--1", 2);
		add(a, ai, "-1--1", 0);
		add(a, ai, "+1-+1", 0);
		add(a, ai, "-1.23-1", -2.23);
		// add(object, i, "f+1", 2.1 );
		add(a, ai, "A4*B5", new Integer(4 * 5));

		// 逻辑运算
		add(a, ai, "5 % 10", 5);
		add(a, ai, "5 % 2", 1);
		add(a, ai, "11 % 5", 1);
		add(a, ai, "a == b", Boolean.FALSE);
		add(a, ai, "a==true", Boolean.TRUE);
		add(a, ai, "a==false", Boolean.FALSE);
		// add(a, ai, "1=='1'", Boolean.TRUE);
		add(a, ai, "1==1.0", Boolean.TRUE);
		// add(a, ai, "1=='1.0'", Boolean.TRUE);
		// add(a, ai, "1.0=='1.0'", Boolean.TRUE);
		add(a, ai, "true==false", Boolean.FALSE);
		add(a, ai, "d==f", Boolean.TRUE);
		add(a, ai, "1+'2'", "12");
		add(a, ai, "1+'2'+1", "121");
		add(a, ai, "'1'+2", "12");
		add(a, ai, "'1'+'2'", "12");
		add(a, ai, "'1'+2+'1'", "121");
		// add(object, i, "'1'*2+'1'", "121" );
		add(a, ai, "1.5-1", 0.5);

		add(a, ai, "2*(4+3)", 14);
		add(a, ai, "2*(4+3)+5*num", 2*(4+3)+5*num);
		
		add(a, ai, "2 < 3", Boolean.TRUE);
		add(a, ai, "num < 5", Boolean.FALSE);
		add(a, ai, "num < num", Boolean.FALSE);
//		add(a, i, "num < null", Boolean.FALSE);
		add(a, ai, "num < 2.5", Boolean.FALSE);
		add(a, ai, "now2 < now", Boolean.FALSE); // test

		// engine,
//		add(a, i, "'6' <= '5'", Boolean.FALSE);
		add(a, ai, "num <= 5", Boolean.TRUE);
		add(a, ai, "num <= num", Boolean.TRUE);
//		add(a, i, "num <= null", Boolean.FALSE);
		add(a, ai, "num <= 2.5", Boolean.FALSE);
		add(a, ai, "now2 <= now", Boolean.FALSE); // test
		// comparable

//		add(a, i, "'6' >= '5'", Boolean.TRUE);
		add(a, ai, "num >= 5", Boolean.TRUE);
		add(a, ai, "num >= num", Boolean.TRUE);
//		add(a, i, "num >= null", Boolean.TRUE);
		add(a, ai, "num >= 2.5", Boolean.TRUE);
		add(a, ai, "now2 >= now", Boolean.TRUE); // test
		// comparable

		// test
//		add(a, i, "'6' > '5'", Boolean.TRUE);
		add(a, ai, "num > 4", Boolean.TRUE);
		add(a, ai, "num > num", Boolean.FALSE);
//		add(a, i, "num > null", Boolean.TRUE);
		add(a, ai, "num > 2.5", Boolean.TRUE);
		add(a, ai, "now2 > now", Boolean.TRUE); // test
		// comparable

		add(a, ai, "\"foo\" + \"bar\" == \"foobar\"", Boolean.TRUE);

		add(a, ai, "bdec > num", Boolean.TRUE);
		add(a, ai, "bdec >= num", Boolean.TRUE);
		add(a, ai, "num <= bdec", Boolean.TRUE);
		add(a, ai, "num < bdec", Boolean.TRUE);
		add(a, ai, "bint > num", Boolean.TRUE);
		add(a, ai, "bint == bdec", Boolean.TRUE);
		add(a, ai, "bint >= num", Boolean.TRUE);
		add(a, ai, "num <= bint", Boolean.TRUE);
		add(a, ai, "num < bint", Boolean.TRUE);

		add(a, ai, "foo == foo", Boolean.TRUE);
		add(a, ai, "foo.foo !=null", Boolean.TRUE);
		add(a, ai, "foo != foo.foo", Boolean.TRUE);

		add(a, ai, "'A' == 'A' || 'B' == 'B' && 'A' == 'A' && 'A' == 'A'",
				Boolean.TRUE);
		add(a, ai, "'A' != 'A' && 'B' == 'B' && 'A' == 'A' && 'A' == 'A'",
				Boolean.FALSE);
		add(a, ai, "!true", !true);
		add(a, ai, "!(1>2)", !(1 > 2));
		add(a, ai, "!(1*2>3)", !(1 * 2 > 3));
		add(a, ai, "true?1:2", 1);
		add(a, ai, "true?false?2:3:1", 3);
		add(a,	ai,"6.7-100>39.6 ? 5==5? 4+5:6-1 : !(100%3-39.0<27) ? 8*2-199: 100%3",1);

		/** **************** Dot operator start **************** */
		add(a, ai, "$('Math').max($('Math').min(1,2),3).doubleValue()", 3.0);
		add(a, ai, "$('" + Foo.class.getName() + "').sayHello('fel')", Foo
				.sayHello("fel"));
		add(a, ai, "$('" + Foo.class.getName() + ".new').get('name')", new Foo()
				.get("name"));
		add(a, ai, "foo.foo", footer);
		add(a, ai, "foo.getCount()", new Integer(header.getCount()));

		add(a, ai, "foo.foo.foo", header);
		add(a, ai, "foo.foo.getCount()", new Integer(footer.getCount()));

		add(a, ai, "foo.getFoo().foo", header);
		add(a, ai, "foo.getFoo().getFoo()", header);
		add(a, ai, "foo.getFoo().getFoo().getFooes()[1]", header.getFoo()
				.getFoo().getFooes()[1]);
		add(a, ai, "foo.getFoo().getFoo().getFooes()[1].name", header.getFoo()
				.getFoo().getFooes()[1].get("name"));

		add(a, ai, "foo.convertBoolean(true)", header.convertBoolean(true));
		add(a,ai,"foo.contact(null,'abc',null,'def')",header.contact(null,"abc",null,"def"));
		
		add(a, ai, "pc.cpu", m.get("cpu"));
		add(a, ai, "pc.memory", m.get("memory"));
		add(a, ai, "pc.get(null)", m.get(null));
		add(a, ai, "pc.put('cpu','intel')", m.get("cpu"));

		/** **************** Dot operator end **************** */
		add(a, ai, "true && 1==2 && (1>2 || 2>1)", Boolean.FALSE);
		add(a, ai, "true && 1==1 && (1>2 || 2>1)", Boolean.TRUE);
		add(a, ai, "true && null", Boolean.FALSE);
		add(a, ai, "null && null", Boolean.FALSE);
		add(a, ai, "null || null", Boolean.FALSE);
		add(a, ai, "null || true", Boolean.TRUE);

		addStringTest(a, ai);
	}

	private Map<String, Object> getVarMap(Foo header, Integer num, Foo footer, Map<String, String> m) {
		header.setFoo(footer);
		footer.setFoo(header);

		Map<String, Object> varMap = new HashMap<String, Object>();
		m.put("cpu", "AMD");
		m.put("memory", "4G");
		m.put(null, "test null key");
		varMap.put("pc", m);

		varMap.put("foo", header);
		varMap.put("a", Boolean.TRUE);
		varMap.put("b", Boolean.FALSE);

		varMap.put("num", num);
		varMap.put("now", Calendar.getInstance().getTime());
		GregorianCalendar gc = new GregorianCalendar(5000, 11, 20);
		varMap.put("now2", gc.getTime());
		varMap.put("bdec", 7);
		varMap.put("bint", 7);
		varMap.put("A4", new Integer(4));
		varMap.put("B5", num);
		float f = 1.1f;
		varMap.put("f", f);
		double d = 1.1d;
		varMap.put("d", d);
		int i = 123456;
		varMap.put("i", i);

		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		indexMap.put("index", 6);
		varMap.put("indexMap", indexMap);
		return varMap;
	}

	private void add(Object[][] array, AtomicInteger i, String exp, Object value) {
		array[i.incrementAndGet()] = new Object[] { exp, value };
	}

	private Object[][] subarray(Object[][] object, int i) {
		return ArrayUtils.subarray(object, 0, i);
	}

	private void addStringTest(Object[][] object, AtomicInteger i) {
		object[i.incrementAndGet()] = new Object[] { "'abc'.indexOf('bc')", 1 };
		object[i.incrementAndGet()] = new Object[] { "'abc'.substring(1)", "bc" };
		object[i.incrementAndGet()] = new Object[] { "'hello world'.substring(indexMap.index)", "world"};
		
	}

	/**
	 * @param engine
	 * @param expr
	 * @param expected
	 */
	@Test(dataProvider = "eval")
	public void testEvalWithCompiler(String expr, Object expected) {
		Expression ins = engine.compile(expr, null);
		Object actual = ins.eval(engine.getContext());
		compare(expected, actual);
	}


	public static void compare(Object expected, Object actual) {
		if (actual instanceof Number && expected instanceof Number) {
			assert NumberUtil.toBigDecimal(actual).equals(NumberUtil.toBigDecimal(expected));
		} else {
			assert ObjectUtils.equals(expected, actual);
		}
	}

	/**
	 * @testng.test dataProvider="eval" group = "script"
	 */
	@Test(dataProvider = "eval")
	protected void testEval(String expression, Object expected) {
		Object actual = engine.eval(expression);
		if (actual instanceof Object[]) {
			Object[] objs = (Object[]) actual;
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < objs.length; j++) {
				buf.append(objs[j]);
				if (j < objs.length - 1)
					buf.append(",");
			}
			actual = buf.toString();
		}
		assert ObjectUtils.equals(expected, actual);
		// assertEquals(expression, expected, actual);
	}

	@Test(dataProvider = "bigEval")
	public void testBigEvalWithCompiler(String expr, Object expected) {
		Expression ins = bigEngine.compile(expr, null);
		Object actual = ins.eval(bigEngine.getContext());
		compare(expected, actual);
	}

	@Test(dataProvider = "bigEval")
	protected void testBigEval(String expression, Object expected) {
		Object actual = bigEngine.eval(expression);
		if (actual instanceof Object[]) {
			Object[] objs = (Object[]) actual;
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < objs.length; j++) {
				buf.append(objs[j]);
				if (j < objs.length - 1)
					buf.append(",");
			}
			actual = buf.toString();
		}
		// assert ObjectUtils.equals(expected, actual);
		compare(expected, actual);
		// assertEquals(expression, expected, actual);
	}


	/**
	 * @testng.data-provider name = "parse"
	 */
	public Object[][] parseData() {
		Object[][] object = new Object[1000][];
		int i = 0;
		object[i++] = new Object[] { "a.b()", "true" };
		object[i++] = new Object[] { "new Date()", "false" };
		return subarray(object, i);
	}

	/**
	 * @testng.test dataProvider="parse" group = "script"
	 */
	protected void testParse(String expression, String expected) {
		FelEngine engine = new FelEngineImpl();
		boolean isPassed = true;
		try {
			engine.parse(expression);
		} catch (Exception e) {
			isPassed = false;
		}
		assert isPassed == new Boolean(expected).booleanValue();
	}

	@Test
	public static void testUserFunction() {
		// 定义hello函数
		Function fun = new CommonFunction() {

			@Override
			public String getName() {
				return "helloFel";
			}

			/*
			 * 调用hello("xxx")时执行的代码
			 */
			@Override
			public Object call(Object[] arguments) {
				Object msg = null;
				if (arguments != null && arguments.length > 0) {
					msg = arguments[0];
				}
				return ObjectUtils.toString(msg);
			}

		};
		FelEngine e = FelEngine.instance;
		// 添加函数到引擎中。
		e.addFun(fun);
		String expected = "fel";
		String exp = "helloFel('" + expected + "')";

		// 解释执行
		Object eval = e.eval(exp);
		assert expected.equals(eval);
		// 编译执行
		Expression compile = e.compile(exp, null);
		eval = compile.eval(null);
		assert expected.equals(eval);
	}

	/**
	 * 
	 */
	// @Test
	public void testConcurrent() {
		int i = 100;
		ExecutorService pool = new ThreadPoolExecutor(0, i, 1L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		final boolean[] result = new boolean[i];
		for (int j = 0; j < i; j++) {
			final int pos = j;
			pool.submit(new Runnable() {
				@Override
				public void run() {
					final FelEngine e = new FelEngineImpl();
					final FelContext ctx = e.getContext();
					Interpreters inte = new Interpreters();
					// 随机生成A,B,C三个字母
					String varName = String.valueOf((char) (65 + (int) (Math
							.random() * 3)));
					final String varValue = varName + "_value";
					inte.add(varName, new Interpreter() {
						@Override
						public Object interpret(FelContext context, FelNode node) {
							return varValue;
						}
					});

					Object value = e.compile(varName, ctx, inte).eval(null);
					// System.out.println(varName+"=>"+(value == varValue));
					result[pos] = value == varValue;
				}

			});
		}
		pool.shutdown();
		while (!pool.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (boolean b : result) {
			assert b;
		}
	}

	public static void main(String[] args) {

		FelEngineImplTest test = new FelEngineImplTest();
		// test.testConcurrent();

		FelEngineImpl engine = new FelEngineImpl();
		Object eval = engine.eval("$('Math').max(1,2)");
		System.out.println(eval);

	}
}
