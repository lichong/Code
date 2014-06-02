package com.wnlc.git.bus.core.netty.transport;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import com.wnlc.git.bus.core.netty.handler.ClientChannelHandler;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private int port;
	private String host;

	public ClientChannelInitializer(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{

		ch.pipeline().addLast(new ClientChannelHandler(host, port));
	}

}
