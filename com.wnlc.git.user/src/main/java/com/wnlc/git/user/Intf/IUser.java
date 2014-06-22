package com.wnlc.git.user.intf;

import com.wnlc.git.user.vo.UserDetail;

public interface IUser
{
	public UserDetail getUserInfo(String account);
}
