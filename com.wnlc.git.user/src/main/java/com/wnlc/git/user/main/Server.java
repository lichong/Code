package com.wnlc.git.user.main;

import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.netty.transport.RemoteServer;
import com.wnlc.git.user.Impl.User;

public class Server
{
	public static void main(String[] args) throws InterruptedException
	{
		CapabilityMgmt.getInstance().addLocalBean(new User());
		new RemoteServer("127.0.0.1", 8090).start();
	}
}
