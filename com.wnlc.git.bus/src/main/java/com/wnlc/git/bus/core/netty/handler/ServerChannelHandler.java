package com.wnlc.git.bus.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wnlc.git.bus.core.netty.context.MessageContext;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter
{
	private static final Logger LOGGER = LogManager.getLogger(ServerChannelHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		MessageContext mc = new MessageContext();
		mc.setContext(ctx);
		mc.setBuf((ByteBuf) msg);
		mc.setRemoteAddr(ctx.channel().remoteAddress().toString());

		LOGGER.info("Receive a msg from remote client." + mc.getRemoteAddr());

		RemoteServerProxy.getInstance().dispatch(mc);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		System.out.println(cause.getMessage());
	}
}
