package com.wnlc.git.bus.core.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class AppEncoder extends ChannelOutboundHandlerAdapter
{
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
	{
		// TODO Auto-generated method stub
		super.write(ctx, msg, promise);
	}
}
