package com.greenpineyu.fel.function.operator.big;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigMod extends BigMul {


	@Override
	public String getName() {
		return "%";
	}

	@Override
	Object calc(BigDecimal left, BigDecimal right) {
		return left.remainder(right);
	}

	@Override
	Object calc(BigInteger left, BigInteger right) {
		return left.remainder(right);
	}
}
