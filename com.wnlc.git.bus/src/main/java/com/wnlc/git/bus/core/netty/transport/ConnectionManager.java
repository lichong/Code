package com.wnlc.git.bus.core.netty.transport;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wnlc.git.bus.core.netty.context.MessageContext;

public class ConnectionManager
{
	private static ConnectionManager INSTANCE = new ConnectionManager();

	private final Map<String, List<ChannelHandlerContext>> connections = new ConcurrentHashMap<String, List<ChannelHandlerContext>>();

	private final Map<String, MessageContext> contexts = new ConcurrentHashMap<String, MessageContext>();

	private ConnectionManager()
	{
	}

	public static ConnectionManager getInstance()
	{
		return INSTANCE;
	}

	public void addConnection(String ip, int port, ChannelHandlerContext channel)
	{
		List<ChannelHandlerContext> channels = connections.get(ip + ":" + port);
		if (channels == null)
		{
			channels = new ArrayList<ChannelHandlerContext>();
			connections.put(ip + ":" + port, channels);
		}
		channels.add(channel);

	}

	public List<ChannelHandlerContext> getConnection(String ip, int port)
	{
		List<ChannelHandlerContext> channels = connections.get(ip + ":" + port);
		if (channels == null)
		{
			channels = new ArrayList<ChannelHandlerContext>();
			connections.put(ip + ":" + port, channels);
			try
			{
				new RemoteClient(ip, port).start();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return channels;
	}

	public void addContext(MessageContext context)
	{
		contexts.put(context.getSeqid(), context);
	}

	public MessageContext getContext(String seqid)
	{
		return contexts.remove(seqid);
	}
}
