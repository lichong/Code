package com.wnlc.git.ose.intf;

import com.wnlc.git.ose.vo.ContentInfo;

public interface IContent
{
	public ContentInfo getContentInfo(String account);

	public ContentInfo getAccountInfo(long size);

	public ContentInfo getAccountInfo2(Long size);

	public ContentInfo getAccountInfo3(String account, long size);
}
