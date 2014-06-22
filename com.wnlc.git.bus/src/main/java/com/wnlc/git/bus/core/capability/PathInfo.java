package com.wnlc.git.bus.core.capability;

public class PathInfo
{
	private int depth = 0;
	private String path;

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PathInfo [depth=");
		builder.append(depth);
		builder.append(", path=");
		builder.append(path);
		builder.append("]");
		return builder.toString();
	}
}
