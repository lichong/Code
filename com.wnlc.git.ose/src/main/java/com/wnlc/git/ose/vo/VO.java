package com.wnlc.git.ose.vo;

public class VO
{
	private String vo;

	public String getVo()
	{
		return vo;
	}

	public void setVo(String vo)
	{
		this.vo = vo;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("VO [vo=");
		builder.append(vo);
		builder.append("]");
		return builder.toString();
	}
}
