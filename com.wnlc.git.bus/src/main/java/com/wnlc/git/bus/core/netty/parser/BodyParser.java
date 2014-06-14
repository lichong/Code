package com.wnlc.git.bus.core.netty.parser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.wnlc.git.bus.core.netty.context.MessageContext;
import com.wnlc.git.bus.core.netty.transport.ConnectionManager;

public class BodyParser
{
	private static final Logger LOGGER = LogManager.getLogger(BodyParser.class);
	private static BodyParser INSTANCE = new BodyParser();
	private static Gson GSON = new Gson();

	private BodyParser()
	{
	}

	public static BodyParser getInstance()
	{
		return INSTANCE;
	}

	public void sendMsg(MessageContext mc, boolean server, Object... args)
	{
		ChannelHandlerContext ctx = mc.getContext();

		ByteBuf buf = ctx.alloc().buffer();
		// seqid
		buf.writeShort(32);
		buf.writeBytes(mc.getSeqid().getBytes());

		// 服务端发送消息时不用携带接口名和方法名
		if (!server)
		{
			// methodName
			buf.writeShort(mc.getMethodName().length());
			buf.writeBytes(mc.getMethodName().getBytes());

			// intfName
			buf.writeShort(mc.getIntfName().length());
			buf.writeBytes(mc.getIntfName().getBytes());

			if (args != null)
			{
				for (Object arg : args)
				{
					byte[] b = (GSON.toJson(arg)).getBytes();
					buf.writeInt(b.length);
					buf.writeBytes(b);
				}
			}
		}
		else
		{
			if (mc.getResult() != null)
			{
				byte[] result = GSON.toJson(mc.getResult()).getBytes();
				buf.writeInt(result.length);
				buf.writeBytes(result);
			}
		}

		if (ctx.channel().isActive())
		{
			ctx.writeAndFlush(buf);
		}
		else
		{
			LOGGER.error("ctx is unactive.");
		}
	}

	public MessageContext receiveMsg(MessageContext mc, ByteBuf buff, boolean server)
	{
		try
		{
			short seqIDLen = buff.readShort();
			byte[] dest = new byte[seqIDLen];
			buff.readBytes(dest, 0, seqIDLen);

			String id = new String(dest, 0, seqIDLen, "UTF-8");
			if (mc == null)
			{
				mc = ConnectionManager.getInstance().getContext(id);
			}
			mc.setSeqid(id);

			int len = 0;
			// 服务端收到消息的时候需要解析方法名和接口名，客户端不需要
			if (server)
			{
				// methodName
				len = buff.readShort();
				dest = new byte[len];
				buff.readBytes(dest);
				mc.setMethodName(new String(dest, 0, len, "UTF-8"));

				// intfName
				len = buff.readShort();
				dest = new byte[len];
				buff.readBytes(dest);
				mc.setIntfName(new String(dest, 0, len, "UTF-8"));
				List<String> args = new ArrayList<String>();
				while (buff.readableBytes() > 0)
				{
					len = buff.readInt();
					dest = new byte[len];
					buff.readBytes(dest, 0, len);
					args.add(new String(dest, 0, len));
				}
				mc.setArgsStr(args.toArray(new String[args.size()]));
			}
			else
			{
				if (buff.readableBytes() > 0)
				{
					len = buff.readInt();
					dest = new byte[len];
					buff.readBytes(dest, 0, len);
					Class<?> resultClazz = mc.getMethod().getReturnType();
					Object result = GSON.fromJson(new String(dest, 0, len), resultClazz);
					mc.setResult(result);
				}
			}

			return mc;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return mc;
	}

	public static void main(String[] args) throws Throwable
	{
		String src = "abc";
		String a = GSON.toJson(src);
		System.out.println(a);

		System.out.println(GSON.fromJson(a, String.class));

		String[] aa = new String[]
		{ "1", "2", "3" };
		a = GSON.toJson(aa);
		System.out.println(a);

		System.out.println(Arrays.toString(GSON.fromJson(a, String[].class)));

		Method m = BodyParser.class.getDeclaredMethod("test", Test1.class, Test2.class);
		m.getParameterTypes();
		Object[] bb = new Object[]
		{ new Test1(), new Test2() };
		a = GSON.toJson(bb);
		System.out.println(a);

		Object[] fromJson = GSON.fromJson(a, m.getParameterTypes().getClass());
		for (Object o : fromJson)
		{
			System.out.println(o);
		}
		System.out.println(Arrays.toString(fromJson));
	}

	public void test(Test1 t1, Test2 t2)
	{

	}
}

class Test1
{
	public Test1()
	{
		name = "test1";
	}

	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Test1 [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
}

class Test2
{
	public Test2()
	{
		test = "test2";
	}

	private String test;

	public String getTest()
	{
		return test;
	}

	public void setTest(String test)
	{
		this.test = test;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Test2 [test=");
		builder.append(test);
		builder.append("]");
		return builder.toString();
	}

}