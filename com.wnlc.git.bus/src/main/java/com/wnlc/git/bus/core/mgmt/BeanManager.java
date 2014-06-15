package com.wnlc.git.bus.core.mgmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.capability.Bus;
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
			CapabilityMgmt.getInstance().addLocalBean(bean);
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
		List<String> intfLst = new ArrayList<String>(intfs);
		Collections.sort(intfLst);
		StringBuffer sb = new StringBuffer();
		for (String intf : intfLst)
		{
			sb.append(",").append(intf);
		}

		byte[] data = null;
		if (sb.length() > 0)
		{
			data = sb.substring(1).getBytes();
			StringBuffer path = new StringBuffer();
			path.append(RegistryMgmt.ROOT_PATH).append("/").append(RegistryMgmt.getInstance().getCapName());
			path.append("/").append(data.hashCode());
			RegistryMgmt.getInstance().getClient().createNode(path.toString(), data);
			path.append("/").append(Bus.getInstance().getServerAddr());
			RegistryMgmt.getInstance().getClient().createEphemeralNode(path.toString());
		};

	}

	public Map<String, Object> getBeanMap()
	{
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		return map;

	}

}
