package com.wnlc.git.ose.mgmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanManager
{
	private List<Object> beans = new ArrayList<Object>();

	public List<Object> getBeans()
	{
		return beans;
	}

	public void setBeans(List<Object> beans)
	{
		this.beans = beans;
	}

	public Map<String, Object> getBeanMap()
	{
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		for (Object bean : beans)
		{
			Class<?>[] clazzs = bean.getClass().getInterfaces();
			for (Class<?> clazz : clazzs)
			{
				map.put(clazz.getSimpleName(), bean);
			}
		}

		return map;

	}
}
