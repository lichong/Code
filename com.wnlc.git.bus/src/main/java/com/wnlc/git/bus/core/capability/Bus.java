package com.wnlc.git.bus.core.capability;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.transport.RemoteServer;

public class Bus
{
	private static final Logger LOGGER = LogManager.getLogger(Bus.class);
	private static Bus INSTANCE = new Bus();
	private String ip;
	private int port;
	private String zookeeperAddr;
	private String capName;

	private Bus()
	{
	}

	public static Bus getInstance()
	{
		return INSTANCE;
	}

	public void start() throws InterruptedException
	{
		RegistryMgmt registryMgmt = RegistryMgmt.getInstance();
		registryMgmt.setZookeeperAddr(zookeeperAddr);
		registryMgmt.init();
		registryMgmt.setCapName(capName);

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

	public String getZookeeperAddr()
	{
		return zookeeperAddr;
	}

	public void setZookeeperAddr(String zookeeperAddr)
	{
		this.zookeeperAddr = zookeeperAddr;
	}

	public String getCapName()
	{
		return capName;
	}

	public void setCapName(String capName)
	{
		this.capName = capName;
	}
}
