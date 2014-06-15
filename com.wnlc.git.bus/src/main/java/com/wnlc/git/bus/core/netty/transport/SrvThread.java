package com.wnlc.git.bus.core.netty.transport;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.capability.ServiceBean;
import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.parser.BodyParser;

public class SrvThread implements Runnable
{
	private static final Logger LOGGER = LogManager.getLogger(SrvThread.class);
	private static final Gson GSON = new Gson();
	private MessageContext mc;

	public SrvThread(MessageContext mc)
	{
		this.mc = mc;
	}

	@Override
	public void run()
	{
		BodyParser.getInstance().receiveMsg(mc, mc.getBuf(), true);

		try
		{
			System.out.println(mc);
			String intfName = mc.getIntfName();
			String methodName = mc.getMethodName();
			String[] argsStr = mc.getArgsStr();
			ServiceBean bean = CapabilityMgmt.getInstance().getBean(intfName);
			Method targetMethod = null;
			Method[] ms = bean.getClazz().getDeclaredMethods();
			for (Method m : ms)
			{
				if (m.getName().equals(methodName))
				{
					targetMethod = m;
					break;
				}
			}
			Object[] args = new Object[argsStr.length];
			Class<?>[] paramters = targetMethod.getParameterTypes();
			for (int i = 0; i < paramters.length; i++)
			{
				args[i] = GSON.fromJson(argsStr[i], paramters[i]);
			}
			mc.setArgs(args);

			Object result = targetMethod.invoke(bean.getBean(), args);
			// String rsp = "Invoke " + intfName + "." + methodName + "(" + (args == null ? "" : Arrays.toString(args))
			// +
			// ")";
			mc.setResult(result);
		}
		catch (Throwable e)
		{
			LOGGER.error("invoke failed.", e);
		}

		BodyParser.getInstance().sendMsg(mc, true);
	}
}
