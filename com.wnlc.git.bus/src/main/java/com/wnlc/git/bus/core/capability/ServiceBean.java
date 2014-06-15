package com.wnlc.git.bus.core.capability;

import java.util.ArrayList;
import java.util.List;

public class ServiceBean
{
	private Class<?> clazz;
	private Object bean;
	private List<String> remoteAddr = new ArrayList<String>();

	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public Object getBean()
	{
		return bean;
	}

	public void setBean(Object bean)
	{
		this.bean = bean;
	}

	public List<String> getRemoteAddr()
	{
		return remoteAddr;
	}

	public void setRemoteAddr(List<String> remoteAddr)
	{
		this.remoteAddr = remoteAddr;
	}

}
