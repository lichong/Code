package com.wnlc.git.user.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDetail
{
	private String account;
	private String name;
	private String passwd;

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPasswd()
	{
		return passwd;
	}

	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("UserDetail [account=");
		builder.append(account);
		builder.append(", name=");
		builder.append(name);
		builder.append(", passwd=");
		builder.append(passwd);
		builder.append("]");
		return builder.toString();
	}
}
