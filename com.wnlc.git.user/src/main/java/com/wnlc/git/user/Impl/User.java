package com.wnlc.git.user.Impl;

import com.wnlc.git.user.Intf.IUser;
import com.wnlc.git.user.vo.UserDetail;

public class User implements IUser
{

	@Override
	public UserDetail getUserInfo(String account)
	{
		UserDetail result = new UserDetail();
		result.setAccount(account);
		result.setName(account);
		result.setPasswd("test");
		return result;
	}

}
