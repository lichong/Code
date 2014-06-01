package com.wnlc.git.ose.impl;

import com.wnlc.git.ose.intf.IContent;
import com.wnlc.git.ose.vo.ContentInfo;

public class Content implements IContent
{

	@Override
	public ContentInfo getContentInfo(String account)
	{
		ContentInfo info = new ContentInfo();
		info.setPath(account + "_path_getContentInfo");
		info.setContent("test.test");
		return info;
	}

	@Override
	public ContentInfo getAccountInfo(long size)
	{
		ContentInfo info = new ContentInfo();
		info.setPath(size + "_path_getAccountInfo");
		info.setContent("test.test");
		return info;
	}

	@Override
	public ContentInfo getAccountInfo2(Long size)
	{
		ContentInfo info = new ContentInfo();
		info.setPath(size + "_path_getAccountInfo2");
		info.setContent("test.test");
		return info;
	}

	@Override
	public ContentInfo getAccountInfo3(String account, long size)
	{
		ContentInfo info = new ContentInfo();
		info.setPath(account + "_" + size + "_path_getAccountInfo3");
		info.setContent("test.test");
		return info;
	}

}
