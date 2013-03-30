package com.greenpineyu.fel.function.operator.big;


public class BigGreaterThanEqual extends BigLessThanEqual {
	

	@Override
	public String getName() {
		return ">=";
	}

	@Override
	protected boolean compare(int value) {
		return value >= 0;
	}

	@Override
	protected boolean nullReturnValue() {
		return false;
	}

	@Override
	protected boolean equalsReturnValue() {
		return true;
	}

}
