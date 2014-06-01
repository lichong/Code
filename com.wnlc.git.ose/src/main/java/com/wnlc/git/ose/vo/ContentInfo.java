package com.wnlc.git.ose.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContentInfo
{
	private String path;
	private String content;
	private ContentInfo info;

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public ContentInfo getInfo()
	{
		return info;
	}

	public void setInfo(ContentInfo info)
	{
		this.info = info;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ContentInfo [path=");
		builder.append(path);
		builder.append(", content=");
		builder.append(content);
		builder.append(", info=");
		builder.append(info);
		builder.append("]");
		return builder.toString();
	}
}
