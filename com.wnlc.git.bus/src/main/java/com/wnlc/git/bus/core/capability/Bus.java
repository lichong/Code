package com.wnlc.git.bus.core.capability;

import com.wnlc.git.bus.core.netty.transport.RemoteServer;

public class Bus
{
	private static Bus INSTANCE = new Bus();

	private Bus()
	{
		// TODO Auto-generated constructor stub
	}

	public static Bus getInstance()
	{
		return INSTANCE;
	}

	public void start()
	{
		new RemoteServer("127.0.0.1", 8090);
	}
}
