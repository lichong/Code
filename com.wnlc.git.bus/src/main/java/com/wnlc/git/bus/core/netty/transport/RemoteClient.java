package com.wnlc.git.bus.core.netty.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

public class RemoteClient
{
	private int port;
	private String host;

	public RemoteClient(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public void start() throws InterruptedException
	{
		List<ChannelFuture> lst = new ArrayList<ChannelFuture>();
		for (int i = 0; i < 10; i++)
		{
			try
			{
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				Bootstrap b = new Bootstrap(); // (1)
				b.group(workerGroup); // (2)
				b.channel(NioSocketChannel.class); // (3)
				b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
				b.handler(new ClientChannelInitializer(host, port));

				// Start the client.
				ChannelFuture f = b.connect(host, port).sync(); // (5)
				lst.add(f);
				// Wait until the connection is closed.
				// f.channel().closeFuture().sync();
			}
			finally
			{
				// workerGroup.shutdownGracefully();
			}
		}
		for (ChannelFuture f : lst)
		{
			while (!f.channel().isActive())
			{
				Thread.sleep(1000);
			}
		}
	}
}
