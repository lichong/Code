package com.wnlc.git.bus.core.mgmt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.capability.RegistryMgmt;

public class BeanManager
{
	private static final Logger LOGGER = LogManager.getLogger(BeanManager.class);
	private List<Object> beans;

	public void setBeans(List<Object> beans)
	{
		this.beans = beans;
	}

	public void init()
	{
		for (Object bean : beans)
		{
			CapabilityMgmt.getInstance().addBean(bean);
		}
		while (!RegistryMgmt.getInstance().isStarted())
		{
			try
			{
				LOGGER.info("Bus does not started yet. Wait 1 second.");
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
		}
		Set<String> intfs = CapabilityMgmt.getInstance().getCapabilityIntfs();
		StringBuffer sb = new StringBuffer();
		for (String intf : intfs)
		{
			sb.append(",").append(intf);
		}

		byte[] data = null;
		if (sb.length() > 0)
		{
			data = sb.substring(1).getBytes();
			RegistryMgmt
					.getInstance()
					.getClient()
					.createNode(
							RegistryMgmt.ROOT_PATH + "/" + RegistryMgmt.getInstance().getCapName() + "/"
									+ data.hashCode(), data);
		};

	}

	public Map<String, Object> getBeanMap()
	{
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		return map;

	}

}
