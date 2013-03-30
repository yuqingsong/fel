package com.greenpineyu.fel.function.operator.big;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDiv extends BigMul {

	public BigDiv(int setPrecision) {
		mc = new MathContext(setPrecision, RoundingMode.HALF_DOWN);
	}

	public BigDiv() {
		this(100);
	}

	private final MathContext mc;


	@Override
	public String getName() {
		return "/";
	}

	@Override
	Object calc(BigDecimal left, BigDecimal right) {
		return left.divide(right, mc);
	}

	@Override
	Object calc(BigInteger left, BigInteger right) {
		return left.divide(right);
	}




}
