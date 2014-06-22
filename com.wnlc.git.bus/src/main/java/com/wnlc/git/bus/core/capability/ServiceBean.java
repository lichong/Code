package com.wnlc.git.bus.core.capability;

import java.util.ArrayList;
import java.util.List;

public class ServiceBean
{
	private Class<?> clazz;
	private Object bean;
	private List<String> remoteAddr = new ArrayList<String>();
	private String capName;

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

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceBean [clazz=");
		builder.append(clazz);
		builder.append(", bean=");
		builder.append(bean);
		builder.append(", remoteAddr=");
		builder.append(remoteAddr);
		builder.append(", capName=");
		builder.append(capName);
		builder.append("]");
		return builder.toString();
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
