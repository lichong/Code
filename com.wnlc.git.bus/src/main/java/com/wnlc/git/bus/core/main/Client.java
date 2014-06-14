package com.wnlc.git.bus.core.main;

import com.wnlc.git.bus.core.netty.handler.RemoteClientProxyHandler;

public class Client
{
	public static void main(String[] args) throws InterruptedException
	{
		ITest test = new RemoteClientProxyHandler<ITest>(ITest.class).getProxy();
		System.out.println(test.test("test_arg", 100));
		Thread.sleep(20000);
		System.out.println(test.test("test_arg", 100));
	}
}
