package com.wnlc.git.ose.mgmt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.capability.Bus;

public class Init
{
	private static final Logger LOGGER = LogManager.getLogger(Init.class);
	private String ip;
	private int port;
	private String zookeeperAddr;

	public void init() throws InterruptedException
	{
		LOGGER.info("Begin to init Method.");
		Bus bus = Bus.getInstance();
		bus.setIp(ip);
		bus.setPort(port);
		bus.setZookeeperAddr(zookeeperAddr);

		bus.start();
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

	public String getZookeeperAddr()
	{
		return zookeeperAddr;
	}

	public void setZookeeperAddr(String zookeeperAddr)
	{
		this.zookeeperAddr = zookeeperAddr;
	}
}
