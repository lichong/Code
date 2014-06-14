package com.wnlc.git.bus.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.parser.BodyParser;
import com.wnlc.git.bus.core.netty.transport.ConnectionManager;
import com.wnlc.git.bus.core.netty.transport.RemoteClient;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger(ClientChannelHandler.class);
	private int port;
	private String host;

	public ClientChannelHandler(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		try
		{
			MessageContext mc = BodyParser.getInstance().receiveMsg(null, (ByteBuf) msg, false);
			synchronized (mc)
			{
				mc.notify();
			}
		}
		finally
		{
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ConnectionManager.getInstance().addConnection(host, port, ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		LOGGER.warn("CTX is unactive");
		ConnectionManager.getInstance().removeChannel(host, port, ctx);
		new RemoteClient(host, port).connect(1);
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		LOGGER.error("Catch an Exception:" + cause.getMessage());
	}

}
