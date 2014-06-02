package com.wnlc.git.bus.core.netty.transport;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import com.wnlc.git.bus.core.netty.handler.ServerChannelHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		// ch.pipeline().addLast(new AppDecoder());
		ch.pipeline().addLast(new ServerChannelHandler());
	}

}
