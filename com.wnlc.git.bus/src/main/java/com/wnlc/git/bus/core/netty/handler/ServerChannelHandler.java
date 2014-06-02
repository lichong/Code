package com.wnlc.git.bus.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.wnlc.git.bus.core.netty.context.MessageContext;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		MessageContext mc = new MessageContext();
		mc.setContext(ctx);
		mc.setBuf((ByteBuf) msg);

		RemoteServerProxy.getInstance().dispatch(mc);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		System.out.println(cause.getMessage());
	}
}
