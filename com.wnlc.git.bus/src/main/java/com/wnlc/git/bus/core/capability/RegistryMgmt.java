package com.wnlc.git.bus.core.capability;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event;

public class RegistryMgmt
{
	private static final Logger LOGGER = LogManager.getLogger(RegistryMgmt.class);
	private static RegistryMgmt INSTANCE = new RegistryMgmt();
	private String zookeeperAddr;
	private RegistryClient client;
	private boolean isStarted = false;
	private String capName;

	public static final String ROOT_PATH = "/com.wnlc.git";

	private RegistryMgmt()
	{
	}

	public static RegistryMgmt getInstance()
	{
		return INSTANCE;
	}

	private void initCapabilities()
	{
		LOGGER.info("Begin to Init all Capabilities.");
		List<String> children = client.getChildren(ROOT_PATH);
		if (children == null)
		{
			LOGGER.info("There's no capability on zookeeper.");
			return;
		}
		for (String child : children)
		{
			List<String> hashs = client.getChildren(ROOT_PATH + "/" + child);
			if (hashs == null)
			{
				LOGGER.info("There's no " + child + "'s hash on zookeeper.");
				continue;
			}
			for (String hash : hashs)
			{
				List<String> ips = client.getChildren(ROOT_PATH + "/" + child + "/" + hash);
				if (ips == null)
				{
					LOGGER.info("There's no " + hash + "'s ip on zookeeper.");
					continue;
				}
				byte[] data = client.getData(ROOT_PATH + "/" + child + "/" + hash, true);
				if (data == null)
				{
					continue;
				}
				String intfs = new String(data);
				String[] intfArr = intfs.split(",");
				for (String intf : intfArr)
				{
					try
					{
						CapabilityMgmt.getInstance().addRemoteBean(child, Class.forName(intf));
					}
					catch (ClassNotFoundException e)
					{
						LOGGER.error("Class Not Found:" + intf, e);
					}
				}
			}
		}
	}

	public void init()
	{
		client = new RegistryClient(zookeeperAddr);
		client.setConnectionStateListener(new ConnectionStateListener() {

			@Override
			public void stateChanged(CuratorFramework arg0, ConnectionState state)
			{
				if (state == ConnectionState.RECONNECTED)
				{
					initCapabilities();
				}
				LOGGER.info("state Changed:" + state);
			}

		});

		client.setCuratorListener(new CuratorListener() {

			@Override
			public void eventReceived(CuratorFramework curator, CuratorEvent event) throws Exception
			{
				LOGGER.info("Received an event:" + event.getWatchedEvent());
				WatchedEvent watchEvent = event.getWatchedEvent();
				if (watchEvent.getPath() == null && watchEvent.getType() == Event.EventType.None)
				{
					return;
				}

				String path = event.getWatchedEvent().getPath();
				curator.getData().watched().forPath(path);
				dealWithPath(path);
			}

		});
		client.start();
		client.createNode(ROOT_PATH);
		isStarted = true;
		initCapabilities();
	}

	private void dealWithPath(String path)
	{
		LOGGER.info("Begin to deal with Path:" + path);
	}

	public String getZookeeperAddr()
	{
		return zookeeperAddr;
	}

	public void setZookeeperAddr(String zookeeperAddr)
	{
		this.zookeeperAddr = zookeeperAddr;
	}

	public boolean isStarted()
	{
		return isStarted;
	}

	public RegistryClient getClient()
	{
		return client;
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
