package com.wnlc.git.bus.core.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;

public class RegistryClient
{
	private static final Logger LOGGER = LogManager.getLogger(RegistryClient.class);
	private CuratorFramework zkClient;
	private ConnectionStateListener connectionStateListener;
	private CuratorListener curatorListener;
	private InterProcessMutex mutex;

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

	public void initLock(String lockPath)
	{
		createNode(lockPath);
		mutex = new InterProcessMutex(zkClient, lockPath);
	}

	public boolean getLock(long time, TimeUnit unit)
	{
		try
		{
			return mutex.acquire(time, unit);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to acquire lock.", e);
		}
		LOGGER.info("get lock success.");
		return false;
	}

	public void getLock()
	{
		try
		{
			mutex.acquire();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to acquire lock.", e);
		}
		LOGGER.info("get lock success.");
	}

	public void releaseLock()
	{
		try
		{
			mutex.release();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to release lock.", e);
		}
		LOGGER.info("release lock success.");
	}

	public void createNode(String path)
	{
		createNode(path, null);
	}

	public void createEphemeralNode(String path)
	{
		try
		{
			if (isExist(path))
			{
				deleteNode(path);
			}
			zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(path);
			LOGGER.info("Create path success:" + path);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to create EPHEMERAL Node." + path, e);
		}
	}

	private void createNodeNotExist(String path, byte[] val)
	{
		try
		{
			if (val == null)
			{
				zkClient.create().forPath(path);
			}
			else
			{
				zkClient.create().forPath(path, val);
			}
			LOGGER.info("Create path success:" + path);
			zkClient.getData().watched().forPath(path);
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to create path:" + path);
		}
	}

	public void createNode(String path, byte[] val)
	{
		try
		{
			LOGGER.info("Start to create path:" + path);
			if (isExist(path))
			{
				LOGGER.info("Node has already exist:" + path);
				if (val != null)
				{
					zkClient.setData().forPath(path, val);
				}
			}
			else
			{
				// if (val == null)
				// {
				// zkClient.create().creatingParentsIfNeeded().forPath(path);
				int index = path.lastIndexOf("/");
				if (index > 0)
				{
					String parentPath = path.substring(0, index);
					createNode(parentPath);
					createNodeNotExist(path, val);
				}
				else
				{
					createNodeNotExist(path, null);
				}

				// }
				// else
				// {
				// zkClient.create().creatingParentsIfNeeded().forPath(path, val);
				// }
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
			return zkClient.checkExists().watched().forPath(path) != null;
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
			return zkClient.getChildren().watched().forPath(path);
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
