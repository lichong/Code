package com.wnlc.git.user.mgmt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.transport.RemoteServer;

public class Init
{
	private static final Logger LOGGER = LogManager.getLogger(Init.class);
	private String ip;
	private int port;

	public void init() throws InterruptedException
	{
		LOGGER.info("Begin to init Method.");
		new RemoteServer(ip, port).start();
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
}
