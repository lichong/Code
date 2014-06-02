package com.wnlc.git.bus.core.capability;

public class CapabilityMgmt
{
	private static CapabilityMgmt INSTANCE = new CapabilityMgmt();

	private CapabilityMgmt()
	{
	}

	public static CapabilityMgmt getInstance()
	{
		return INSTANCE;
	}

}
