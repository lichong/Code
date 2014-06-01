package com.wnlc.git.ose.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.ose.intf.ICatalog;
import com.wnlc.git.ose.vo.GetDiskReq;
import com.wnlc.git.ose.vo.GetDiskRsp;
import com.wnlc.git.ose.vo.VO;

public class Catalog implements ICatalog
{

	@Override
	public GetDiskRsp getDisk(GetDiskReq req)
	{
		GetDiskRsp rsp = new GetDiskRsp();
		rsp.setName("test");
		rsp.setSize(1000);;
		rsp.setVo(new VO());
		return rsp;
	}

	public static void main(String[] args)
	{
		final Logger logger = LogManager.getLogger(Catalog.class.getName());
		logger.info("TEST");
	}
}
