package com.wnlc.git.ose.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetDiskRsp
{
	private String name;
	private long size;
	private VO vo;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("GetDiskRsp [name=");
		builder.append(name);
		builder.append(", size=");
		builder.append(size);
		builder.append(", vo=");
		builder.append(vo);
		builder.append("]");
		return builder.toString();
	}

	public VO getVo()
	{
		return vo;
	}

	public void setVo(VO vo)
	{
		this.vo = vo;
	}

}
