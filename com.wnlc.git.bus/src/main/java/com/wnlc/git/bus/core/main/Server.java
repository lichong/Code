package com.wnlc.git.bus.core.main;

import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.netty.transport.RemoteServer;

public class Server
{
	public static void main(String[] args) throws InterruptedException
	{
		CapabilityMgmt.getInstance().addLocalBean(new Test());
		new RemoteServer("127.0.0.1", 8090).start();
	}
}
