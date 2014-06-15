package com.wnlc.git.bus.core.capability;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.handler.RemoteClientProxyHandler;

public class CapabilityMgmt
{
	private static final Logger LOGGER = LogManager.getLogger(CapabilityMgmt.class);
	private static CapabilityMgmt INSTANCE = new CapabilityMgmt();

	private static final Map<String, ServiceBean> beans = new ConcurrentHashMap<String, ServiceBean>();

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
			ServiceBean serviceBean = new ServiceBean();
			serviceBean.setBean(bean);
			serviceBean.setClazz(clazz);
			beans.put(clazz.getName(), serviceBean);
		}
	}

	public void addRemoteBean(String capName, Class<?> intf)
	{
		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setBean(new RemoteClientProxyHandler(intf).getProxy());
		serviceBean.setClazz(intf);
		beans.put(capName + "." + intf.getSimpleName(), serviceBean);
	}

	public Set<String> getCapabilityIntfs()
	{
		return beans.keySet();
	}

	public ServiceBean getBean(String intfName)
	{
		return beans.get(intfName);
	}

}
