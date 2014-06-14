package com.wnlc.git.bus.core.mgmt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wnlc.git.bus.core.capability.CapabilityMgmt;

public class BeanManager
{
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

	}

	public Map<String, Object> getBeanMap()
	{
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		return map;

	}

}
