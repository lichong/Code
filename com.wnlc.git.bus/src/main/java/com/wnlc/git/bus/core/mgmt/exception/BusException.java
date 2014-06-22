package com.wnlc.git.bus.core.mgmt.exception;

public class BusException extends Exception
{
	private static final long serialVersionUID = 151829764678231101L;
	private String code;
	private String message;

	public BusException(String code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public BusException(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
