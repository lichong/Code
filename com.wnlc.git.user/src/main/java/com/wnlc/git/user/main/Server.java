package com.wnlc.git.user.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server
{
	private static final Logger LOGGER = LogManager.getLogger(Server.class);

	public static void main(String[] args) throws InterruptedException
	{
		LOGGER.info("test");
		// CapabilityMgmt.getInstance().addLocalBean(new User());
		// new RemoteServer("127.0.0.1", 8090).start();
	}
}
