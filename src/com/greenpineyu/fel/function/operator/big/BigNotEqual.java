package com.greenpineyu.fel.function.operator.big;


public class BigNotEqual extends BigEqual {

	@Override
	public String getName() {
		return "!=";
	}

	@Override
	protected boolean compareNumber(Object left, Object right) {
		return !super.compareNumber(left, right);
	}

}
