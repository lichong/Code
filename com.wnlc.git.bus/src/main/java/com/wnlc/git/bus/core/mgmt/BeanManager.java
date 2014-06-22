package com.wnlc.git.bus.core.mgmt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.capability.Bus;
import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.capability.RegistryMgmt;

public class BeanManager
{
	private static final Logger LOGGER = LogManager.getLogger(BeanManager.class);
	private List<Object> beans;
	public static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public void setBeans(List<Object> beans)
	{
		this.beans = beans;
	}

	public void init()
	{

		if (beans == null || beans.isEmpty())
		{
			LOGGER.info("No Beans need to be exported.");
			return;
		}
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
			RegistryMgmt.getInstance().getLock();
			try
			{
				String allIntfs = sb.substring(1);
				data = allIntfs.getBytes();
				String hash = getMD5ForByte(allIntfs);
				StringBuffer path = new StringBuffer();
				path.append(RegistryMgmt.ROOT_PATH).append("/").append(RegistryMgmt.getInstance().getCapName());
				path.append("/").append(hash);
				RegistryMgmt.getInstance().getClient().createNode(path.toString(), data);
				path.append("/").append(Bus.getInstance().getServerAddr());
				RegistryMgmt.getInstance().getClient().createEphemeralNode(path.toString());
			}
			finally
			{
				RegistryMgmt.getInstance().releaseLock();
			}
		};

	}

	private String getMD5ForByte(String intfs)
	{
		byte[] data = intfs.getBytes();
		MessageDigest digest = null;
		try
		{
			digest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			LOGGER.error("Faile to get MD5 from byte.");
			return String.valueOf(intfs.hashCode());
		}
		digest.update(data);
		byte[] digestByte = digest.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digestByte)
		{
			char c0 = hexDigits[(b & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
			char c1 = hexDigits[b & 0xf];// 取字节中低 4 位的数字转换
			sb.append(c0);
			sb.append(c1);
		}
		return sb.toString();
	}
}
