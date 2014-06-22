package com.wnlc.git.bus.core.capability;

import java.util.List;
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

	private static final Map<String, ServiceBean> remoteBeans = new ConcurrentHashMap<String, ServiceBean>();
	private static final Map<String, ServiceBean> localBeans = new ConcurrentHashMap<String, ServiceBean>();

	private static final Map<String, Capability> capabilities = new ConcurrentHashMap<String, Capability>();

	private CapabilityMgmt()
	{
	}

	public static CapabilityMgmt getInstance()
	{
		return INSTANCE;
	}

	public void registerCap(Capability cap)
	{
		capabilities.put(cap.getKey(), cap);
	}

	public void addLocalBean(Object bean)
	{
		Class<?>[] clazzs = bean.getClass().getInterfaces();
		for (Class<?> clazz : clazzs)
		{
			ServiceBean serviceBean = new ServiceBean();
			serviceBean.setBean(bean);
			serviceBean.setClazz(clazz);
			localBeans.put(clazz.getName(), serviceBean);
		}
		LOGGER.info("Add Local Beans Success." + bean.getClass());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRemoteBean(String capName, Class<?> intf, List<String> ips)
	{
		ServiceBean serviceBean = new ServiceBean();
		serviceBean.setClazz(intf);
		serviceBean.setRemoteAddr(ips);
		serviceBean.setCapName(capName);
		RemoteClientProxyHandler handler = new RemoteClientProxyHandler(intf);
		handler.setBean(serviceBean);
		serviceBean.setBean(handler.getProxy());
		remoteBeans.put(capName + "." + intf.getSimpleName(), serviceBean);
		LOGGER.info("Add Remote Beans Success." + capName + "." + intf);
	}

	public Set<String> getCapabilityIntfs()
	{
		return localBeans.keySet();
	}

	public ServiceBean getBean(String intfName)
	{
		ServiceBean result = localBeans.get(intfName);
		if (result != null)
		{
			return result;
		}

		return remoteBeans.get(intfName);
	}

}
