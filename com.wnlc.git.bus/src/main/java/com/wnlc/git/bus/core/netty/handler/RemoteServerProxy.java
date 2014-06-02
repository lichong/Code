package com.wnlc.git.bus.core.netty.handler;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.transport.SrvThread;

public class RemoteServerProxy
{
	private ThreadPoolExecutor executor;
	private static RemoteServerProxy INSTANCE = new RemoteServerProxy();

	public static RemoteServerProxy getInstance()
	{
		return INSTANCE;
	}

	public void init()
	{
		executor = new ThreadPoolExecutor(10, 100, 60000, TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<Runnable>(1000));
	}

	private RemoteServerProxy()
	{

	}

	public void dispatch(MessageContext mc)
	{
		executor.execute(new SrvThread(mc));
	}
}
