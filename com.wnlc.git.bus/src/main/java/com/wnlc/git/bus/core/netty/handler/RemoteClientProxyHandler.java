package com.wnlc.git.bus.core.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.parser.BodyParser;
import com.wnlc.git.bus.core.netty.transport.ConnectionManager;

public class RemoteClientProxyHandler<T> implements InvocationHandler
{
	private static final Logger LOGGER = LogManager.getLogger(RemoteClientProxyHandler.class);
	private Class<T> target;

	public RemoteClientProxyHandler(Class<T> target)
	{
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	public T getProxy()
	{
		return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]
		{ target }, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		List<ChannelHandlerContext> contexts = ConnectionManager.getInstance().getConnection("127.0.0.1", 8090);
		ChannelHandlerContext ctx = contexts.get(0);
		MessageContext mc = new MessageContext();
		mc.setContext(ctx);
		mc.setSeqid(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		ConnectionManager.getInstance().addContext(mc);
		mc.setMethodName(method.getName());
		mc.setIntfName(target.getName());
		mc.setMethod(method);

		BodyParser.getInstance().sendMsg(mc, false, args);

		synchronized (mc)
		{
			while (true)
			{
				mc.wait(10000);
				break;
			}
		}

		return mc.getResult();
	}

}
