package com.wnlc.git.user.Impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.user.Intf.IUser;
import com.wnlc.git.user.vo.UserDetail;

public class User implements IUser
{

	private static final Logger LOGGER = LogManager.getLogger(User.class);

	@Override
	public UserDetail getUserInfo(String account)
	{
		LOGGER.info("Begin to deal request. account=" + account);
		UserDetail result = new UserDetail();
		result.setAccount(account);
		result.setName(account);
		result.setPasswd("test1234");
		return result;
	}

}
