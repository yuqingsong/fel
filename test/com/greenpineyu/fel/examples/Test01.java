package com.greenpineyu.fel.examples;

public class Test01 {

	public static void main(String[] args) {
		int a = 1988;
		System.out.println("a=" + a);
		System.out.println("a 每个位置上的数字");
		System.out.print(a / 1000);
		a %= 1000;
		System.out.print("," + a / 100);
		a %= 100;
		System.out.print("," + a / 10);
		a %= 10;
		System.out.print("," + a);

	}

}
