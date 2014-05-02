package com.greenpineyu.fel.examples;

import java.util.regex.Pattern;

import com.greenpineyu.fel.FelEngine;

class SplitString {
	String SplitStr;
	int splitByte;

	public SplitString(String str, int bytes) {
		SplitStr = str;
		splitByte = bytes;
		System.out.println("The String is:′" + SplitStr + "′;SplitBytes=" + splitByte);
	}

	public void SplitIt() {
		int loopCount;
		loopCount = (SplitStr.length() % splitByte == 0) ? (SplitStr.length() / splitByte) : (SplitStr.length()
				/ splitByte + 1);
		System.out.println("Will Split into " + loopCount);
		for (int i = 1; i <= loopCount; i++) {
			if (i == loopCount) {
				System.out.println(SplitStr.substring((i - 1) * splitByte, SplitStr.length()));
			} else {
				System.out.println(SplitStr.substring((i - 1) * splitByte, (i * splitByte)));
			}
		}
	}

	static public void split(String str, int substringLen) {
		char[] chars = str.toCharArray();
		int byteCount = 0;
		int previewCharIndex = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == (byte) c) {
				byteCount++;
				// System.out.print("单字节   ");
			} else {
				byteCount += 2;
				// System.out.print("双字节   ");
			}
			if (byteCount == substringLen) {
				System.out.println(previewCharIndex + "<=>" + (i + 1) + ":" + str.substring(previewCharIndex, i + 1));
				previewCharIndex = i + 1;
				byteCount = 0;
			} else if (byteCount > substringLen) {
				System.out.println(previewCharIndex + "<=>" + (i + 1) + ":" + str.substring(previewCharIndex, i));
				previewCharIndex = i;
				byteCount = 2;
			}
		}
		if (str.length() != previewCharIndex) {
			System.out.println(str.substring(previewCharIndex, str.length()));
		}
	}

	public static void main(String[] args) {
		// String msg = "test中dd文dsaf中男大3443n中国43中国人 0ewldfls=103";
		// SplitString ss = new SplitString(msg, 4);
		// ss.SplitIt();
		// split(msg, 4);
		// cast();
		// ppp(null);
		boolean matches = Pattern.matches("\\d*", "123456");
		System.out.println(matches);

		Object result = FelEngine.instance.eval("$('java.util.regex.Pattern').matches('\\\\d+', '123456')");
		System.out.println(result);
	}

	public static void cast() {
		char a = (char) -32768;
		System.out.println(Integer.toBinaryString(-32768));
		System.out.println(Integer.toBinaryString((char) -32768));
		System.out.println(Integer.toBinaryString((char) 32767));
		System.out.println(0x80);
		System.out.println(a);
		System.out.println((int) '翿');
		char count = 256;
		for (int i = 0; i < count; i++) {
			char c = (char) i;
			System.out.print(i + ":" + (byte) c + "--");
			System.out.println(Integer.toBinaryString(i) + ":" + Integer.toBinaryString((byte) c));
		}
	}

	static public int f(int n) {
		if (n == 0 || n == 1) {
			// System.out.println(n);
			return n;

		}
		int returnMe = f(n - 1) + f(n - 2);
		// System.out.println(returnMe);
		return returnMe;
	}

	static int count = 0;

	static void ppp(String[] arg) {
		// Scanner r = new Scanner(System.in);
		// String s = r.nextLine();
		Pailie("122345", " ");
		System.out.println("Total: " + count);
	}

	static void Pailie(String s, String p) {
		if (s.length() < 1) {
			System.out.println(p);// 字符串长度小于1，换行
			count++;
		} else {
			int index[] = new int[s.length()];
			for (int i = 0; i < s.length(); i++)
				// 该循环将所有字符的第一次出现的位置记 录在数组index中
				index[i] = s.indexOf(s.charAt(i));
			for (int i = 0; i < s.length(); i++) {
				if (i == index[i])
					// 只有当循环数与第一次记录数相等时才递归，保证相同字符中 的第一个调用
					Pailie(s.substring(1), p + s.substring(0, 1));// 递归，打印其它字符
				s = s.substring(1) + s.substring(0, 1);// 循环移位
			}
		}
	}

	static void pl(String str) {
		char[] chars = new char[] {
				'1', '2', '2', '3', '4', '5' };
		char[] options = new char[] {
				'1', '2', '3', '4', '5' };
		for (int i = 0; i < chars.length; i++) {

		}
	}

}