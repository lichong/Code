package com.wnlc.git.bus.core.capability;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistryClient
{
	private static final Logger LOGGER = LogManager.getLogger(RegistryClient.class);
	private CuratorFramework zkClient;
	private ConnectionStateListener connectionStateListener;
	private CuratorListener curatorListener;

	public RegistryClient(String zookeeperAddr)
	{
		RetryPolicy retryPolicy = new RetryNTimes(10, 1000);
		zkClient = CuratorFrameworkFactory.builder().connectString(zookeeperAddr).connectionTimeoutMs(10000)
				.sessionTimeoutMs(30000).retryPolicy(retryPolicy).build();
	}

	public void start()
	{
		if (connectionStateListener != null)
		{
			zkClient.getConnectionStateListenable().addListener(connectionStateListener);
		}
		if (curatorListener != null)
		{
			zkClient.getCuratorListenable().addListener(curatorListener);
		}
		zkClient.start();
		try
		{
			if (!zkClient.getZookeeperClient().blockUntilConnectedOrTimedOut())
			{
				LOGGER.error("Can't connect to zookeeper.");
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void createNode(String path)
	{
		createNode(path, null);
	}

	public void createNode(String path, byte[] val)
	{
		try
		{
			if (isExist(path))
			{
				zkClient.setData().forPath(path, val);
			}
			else
			{
				if (val == null)
				{
					zkClient.create().creatingParentsIfNeeded().forPath(path);
				}
				else
				{
					zkClient.create().creatingParentsIfNeeded().forPath(path, val);
				}
			}
			zkClient.getData().watched().forPath(path);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to create Node." + path, e);
		}
	}

	public byte[] getData(String path, boolean watch)
	{
		try
		{
			if (watch)
			{
				return zkClient.getData().watched().forPath(path);
			}
			else
			{
				return zkClient.getData().forPath(path);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Faild to getData for " + path, e);
			return null;
		}
	}

	public void deleteNode(String path)
	{
		try
		{
			zkClient.delete().forPath(path);
		}
		catch (Exception e)
		{
			LOGGER.error("Faild to delete Data for " + path, e);
		}
	}

	public boolean isExist(String path)
	{
		try
		{
			return zkClient.checkExists().forPath(path) != null;
		}
		catch (Exception e)
		{
			LOGGER.error("Faild to check exist for " + path, e);
			return false;
		}
	}

	public List<String> getChildren(String path)
	{
		try
		{
			return zkClient.getChildren().forPath(path);
		}
		catch (Exception e)
		{
			LOGGER.error("Faild to getChildren for " + path, e);
			return new ArrayList<String>();
		}
	}

	public ConnectionStateListener getConnectionStateListener()
	{
		return connectionStateListener;
	}

	public void setConnectionStateListener(ConnectionStateListener connectionStateListener)
	{
		this.connectionStateListener = connectionStateListener;
	}

	public CuratorListener getCuratorListener()
	{
		return curatorListener;
	}

	public void setCuratorListener(CuratorListener curatorListener)
	{
		this.curatorListener = curatorListener;
	}

}
