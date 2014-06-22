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
	private RegistryClient lockClient;
	private boolean isStarted = false;
	private String capName;

	public static final String ROOT_PATH = "/com.wnlc.git";
	private static final String LOCK_PATH = "/com.wnlc.lock";

	private RegistryMgmt()
	{
	}

	public static RegistryMgmt getInstance()
	{
		return INSTANCE;
	}

	private void initCapabilities()
	{
		lockClient.getLock();
		try
		{
			LOGGER.info("Begin to Init all Capabilities.");
			List<String> children = client.getChildren(ROOT_PATH);
			if (children == null)
			{
				LOGGER.info("There's no capability on zookeeper.");
				return;
			}
			LOGGER.info("Begin to sync all capabilties:" + children);
			for (String capability : children)
			{
				if (capability.equals(capName))
				{
					LOGGER.info("Current cap is the same as the node." + capability);
					continue;
				}
				String currentPath = ROOT_PATH + "/" + capability;
				List<String> hashs = client.getChildren(currentPath);
				if (hashs == null)
				{
					LOGGER.info("There's no " + currentPath + "'s hash on zookeeper.");
					continue;
				}
				LOGGER.info("Hash of " + currentPath + " are as follows:" + hashs);
				for (String hash : hashs)
				{
					currentPath += "/" + hash;
					List<String> ips = client.getChildren(currentPath);
					if (ips == null)
					{
						LOGGER.info("There's no " + currentPath + "'s ip on zookeeper.");
						continue;
					}
					LOGGER.info("IPS of " + currentPath + " are as follows:" + ips);
					byte[] data = client.getData(currentPath, true);
					if (data == null)
					{
						LOGGER.info("Data of " + currentPath + " is null, igore this node.");
						continue;
					}
					Capability cap = new Capability(capability, hash);
					cap.setIps(ips);
					CapabilityMgmt.getInstance().registerCap(cap);

					String intfs = new String(data);
					String[] intfArr = intfs.split(",");
					for (String intf : intfArr)
					{
						try
						{
							CapabilityMgmt.getInstance().addRemoteBean(capability, Class.forName(intf), ips);
						}
						catch (ClassNotFoundException e)
						{
							LOGGER.error("Class Not Found:" + intf, e);
						}
					}
				}
			}
		}
		finally
		{
			lockClient.releaseLock();
		}
	}

	public void init()
	{
		initZKConClient();

		initZKLockClient();

		isStarted = true;
		initCapabilities();
	}

	private void initZKLockClient()
	{

		lockClient = new RegistryClient(zookeeperAddr);
		lockClient.setConnectionStateListener(new ConnectionStateListener() {

			@Override
			public void stateChanged(CuratorFramework arg0, ConnectionState state)
			{
				if (state == ConnectionState.RECONNECTED)
				{
					lockClient = new RegistryClient(zookeeperAddr);
					lockClient.start();
					lockClient.createNode(LOCK_PATH);
				}
				LOGGER.info("Lock state Changed:" + state);
			}

		});

		lockClient.start();
		lockClient.initLock(LOCK_PATH);
	}

	public void getLock()
	{
		lockClient.getLock();
	}

	public void releaseLock()
	{
		lockClient.releaseLock();
	}

	private void initZKConClient()
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

				String path = watchEvent.getPath();
				curator.getData().watched().forPath(path);
				dealWithPath(watchEvent);
			}

		});
		client.start();
		client.createNode(ROOT_PATH);
	}

	private void dealWithPath(WatchedEvent watchEvent)
	{
		LOGGER.info("Begin to deal with Event:" + watchEvent);
		String path = watchEvent.getPath();
		PathInfo info = analysePath(path);
		switch (watchEvent.getType())
		{
			case NodeCreated:
				break;
			case NodeDeleted:
				break;
			case NodeDataChanged:
				initCapabilities();
				break;
			case NodeChildrenChanged:
				initCapabilities();
				break;

			default:
				break;
		}
	}

	private PathInfo analysePath(String path)
	{
		PathInfo info = new PathInfo();
		int depth = 0;

		for (int i = 0; i < path.length(); i++)
		{
			if (path.charAt(i) == '/')
			{
				depth++;
			}
		}
		info.setDepth(depth);
		info.setPath(path);
		return info;
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
