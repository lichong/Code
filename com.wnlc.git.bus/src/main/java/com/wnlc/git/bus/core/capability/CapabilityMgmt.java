package com.wnlc.git.bus.core.capability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CapabilityMgmt
{
	private static CapabilityMgmt INSTANCE = new CapabilityMgmt();

	private static final Map<String, Object> beans = new ConcurrentHashMap<String, Object>();

	private CapabilityMgmt()
	{
	}

	public static CapabilityMgmt getInstance()
	{
		return INSTANCE;
	}

	public void addBean(Object bean)
	{
		Class<?>[] clazzs = bean.getClass().getInterfaces();
		for (Class<?> clazz : clazzs)
		{
			beans.put(clazz.getSimpleName(), bean);
		}
	}

	public Object getBean(String intfName)
	{
		return beans.get(intfName);
	}

}
