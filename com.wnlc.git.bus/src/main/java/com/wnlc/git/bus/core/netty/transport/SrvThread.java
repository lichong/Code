package com.wnlc.git.bus.core.netty.transport;

import java.util.Arrays;

import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.parser.BodyParser;

public class SrvThread implements Runnable
{
	private MessageContext mc;

	public SrvThread(MessageContext mc)
	{
		this.mc = mc;
	}

	@Override
	public void run()
	{
		BodyParser.getInstance().receiveMsg(mc, mc.getBuf(), true);

		System.out.println(mc);
		String intfName = mc.getIntfName();
		String methodName = mc.getMethodName();
		Object[] args = mc.getArgs();
		String rsp = "Invoke " + intfName + "." + methodName + "(" + (args == null ? "" : Arrays.toString(args)) + ")";
		mc.setResult(rsp);

		BodyParser.getInstance().sendMsg(mc, true);
	}

}
