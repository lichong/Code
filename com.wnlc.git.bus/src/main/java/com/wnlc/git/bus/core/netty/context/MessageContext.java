package com.wnlc.git.bus.core.netty.context;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MessageContext
{
	private ChannelHandlerContext context;
	private String seqid;
	private byte[] msg;
	private ByteBuf buf;
	private byte[] rspMsg;
	private String methodName;
	private String intfName;
	private String[] argsStr;
	private Object[] args;
	private Object result;
	private Method method;
	private String remoteAddr;

	public ChannelHandlerContext getContext()
	{
		return context;
	}

	public void setContext(ChannelHandlerContext context)
	{
		this.context = context;
	}

	public String getSeqid()
	{
		return seqid;
	}

	public void setSeqid(String seqid)
	{
		this.seqid = seqid;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MessageContext [context=");
		builder.append(context);
		builder.append(", seqid=");
		builder.append(seqid);
		builder.append(", msg=");
		builder.append(Arrays.toString(msg));
		builder.append(", buf=");
		builder.append(buf);
		builder.append(", rspMsg=");
		builder.append(Arrays.toString(rspMsg));
		builder.append(", methodName=");
		builder.append(methodName);
		builder.append(", intfName=");
		builder.append(intfName);
		builder.append(", argsStr=");
		builder.append(Arrays.toString(argsStr));
		builder.append(", args=");
		builder.append(Arrays.toString(args));
		builder.append(", result=");
		builder.append(result);
		builder.append(", method=");
		builder.append(method);
		builder.append("]");
		return builder.toString();
	}

	public byte[] getMsg()
	{
		return msg;
	}

	public void setMsg(byte[] msg)
	{
		this.msg = msg;
	}

	public ByteBuf getBuf()
	{
		return buf;
	}

	public void setBuf(ByteBuf buf)
	{
		this.buf = buf;
	}

	public byte[] getRspMsg()
	{
		return rspMsg;
	}

	public void setRspMsg(byte[] rspMsg)
	{
		this.rspMsg = rspMsg;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public String getIntfName()
	{
		return intfName;
	}

	public void setIntfName(String intfName)
	{
		this.intfName = intfName;
	}

	public String[] getArgsStr()
	{
		return argsStr;
	}

	public void setArgsStr(String[] args)
	{
		this.argsStr = args;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}

	public Object[] getArgs()
	{
		return args;
	}

	public void setArgs(Object[] args)
	{
		this.args = args;
	}

	public String getRemoteAddr()
	{
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr)
	{
		this.remoteAddr = remoteAddr;
	}
}
