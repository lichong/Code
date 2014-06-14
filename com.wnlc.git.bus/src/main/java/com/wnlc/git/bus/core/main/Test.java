package com.wnlc.git.bus.core.main;

public class Test implements ITest
{

	@Override
	public String test(String a, int b)
	{
		return a + "|" + b;
	}
}
