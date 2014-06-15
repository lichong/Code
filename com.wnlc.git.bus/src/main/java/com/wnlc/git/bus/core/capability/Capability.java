package com.wnlc.git.bus.core.capability;

import java.util.List;

public class Capability
{
	private List<String> ips;
	private String capName;
	private String hash;
	private String key;

	public Capability(String capName, String hash)
	{
		this.capName = capName;
		this.hash = hash;
		this.key = capName + "." + hash;
	}

	public List<String> getIps()
	{
		return ips;
	}

	public void setIps(List<String> ips)
	{
		this.ips = ips;
	}

	public String getCapName()
	{
		return capName;
	}

	public void setCapName(String capName)
	{
		this.capName = capName;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public String getKey()
	{
		return key;
	}

}
