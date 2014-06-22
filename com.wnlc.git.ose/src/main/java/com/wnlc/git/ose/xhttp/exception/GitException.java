package com.wnlc.git.ose.xhttp.exception;

public class GitException extends Exception
{
	private static final long serialVersionUID = -4256215630019370799L;
	private String code;
	private String message;

	public GitException(String code)
	{
		this.code = code;
	}

	public GitException(String code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public GitException()
	{

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
