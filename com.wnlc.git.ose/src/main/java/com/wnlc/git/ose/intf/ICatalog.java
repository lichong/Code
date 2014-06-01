package com.wnlc.git.ose.intf;

import com.wnlc.git.ose.vo.GetDiskReq;
import com.wnlc.git.ose.vo.GetDiskRsp;

public interface ICatalog
{
	public GetDiskRsp getDisk(GetDiskReq req);
}
