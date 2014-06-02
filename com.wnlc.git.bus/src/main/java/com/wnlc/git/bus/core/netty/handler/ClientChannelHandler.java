package com.wnlc.git.bus.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.parser.BodyParser;
import com.wnlc.git.bus.core.netty.transport.ConnectionManager;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter
{
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

}
