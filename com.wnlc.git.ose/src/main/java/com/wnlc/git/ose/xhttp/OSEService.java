package com.wnlc.git.ose.xhttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wnlc.git.bus.core.capability.CapabilityMgmt;
import com.wnlc.git.bus.core.capability.ServiceBean;
import com.wnlc.git.bus.core.netty.handler.RemoteClientProxyHandler;
import com.wnlc.git.user.Intf.IUser;

public class OSEService extends HttpServlet
{

	private static final Logger LOGGER = LogManager.getLogger(OSEService.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6388952991464503552L;

	private static final Set<Class<?>> primivateClazz = new HashSet<Class<?>>();
	static
	{
		primivateClazz.add(Integer.class);
		primivateClazz.add(Double.class);
		primivateClazz.add(Character.class);
		primivateClazz.add(Byte.class);
		primivateClazz.add(Short.class);
		primivateClazz.add(Long.class);
		primivateClazz.add(Float.class);
		primivateClazz.add(Boolean.class);

	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		ServletContext application = getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
		CapabilityMgmt.getInstance().addBean(new RemoteClientProxyHandler<IUser>(IUser.class).getProxy());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		LOGGER.info(req.getRequestURI());
		String result = null;
		try
		{
			result = doExChangeStream(req);
		}
		catch (Throwable e)
		{
			LOGGER.error("Catch an Exception:", e);
			result = e.getMessage();
		}
		if (result != null)
		{
			resp.getWriter().write(result);
		}
		else
		{
			LOGGER.info("Something ERROR");
			resp.getWriter().write("ERROR");
		}
	}

	private String doExChangeStream(HttpServletRequest req) throws Throwable
	{
		if (req.getContentType().contains("text/xml"))
		{
			InputStream is = null;
			try
			{
				String uri = req.getRequestURI();
				String intfName = uri.substring(uri.lastIndexOf("/") + 1);
				is = req.getInputStream();
				LOGGER.info("Interface:" + intfName);
				SAXReader reader = new SAXReader();
				Document document = reader.read(is);
				if (document == null)
				{
					return null;
				}

				Element root = document.getRootElement();

				String methodName = root.getName();
				LOGGER.info("Method:" + methodName);
				LOGGER.info("~~~~~~~~~~~~~~~~~" + CapabilityMgmt.getInstance().getCapabilityIntfs());
				ServiceBean bean = CapabilityMgmt.getInstance().getBean(intfName);
				Class<?> clazz = bean.getClazz();
				Method[] method = clazz.getDeclaredMethods();
				Method targetMethod = null;
				for (Method m : method)
				{
					if (m.getName().equals(methodName))
					{
						targetMethod = m;
						break;
					}
				}

				if (targetMethod == null)
				{
					String message = "Can't find Method:" + methodName + " for " + intfName;
					LOGGER.error(message);
					throw new Exception(message);
				}
				Class<?>[] parameters = targetMethod.getParameterTypes();
				@SuppressWarnings("unchecked")
				List<Element> elements = root.elements();
				if (parameters.length != elements.size())
				{
					String message = "Parameters " + Arrays.toString(parameters) + " can't match:" + methodName;
					LOGGER.error(message);
					throw new Exception(message);
				}
				Object[] args = new Object[parameters.length];
				for (int i = 0; i < parameters.length; i++)
				{
					String xml = elements.get(i).asXML();
					if (parameters[i] == String.class || isPrimitive(parameters[i]))
					{
						args[i] = createPrimitiveVal(elements.get(i).getTextTrim(), parameters[i]);
					}
					else
					{
						JAXBContext context = JAXBContext.newInstance(parameters[i]);
						Unmarshaller um = context.createUnmarshaller();
						args[i] = um.unmarshal(new StringReader(xml));
					}
				}
				LOGGER.info("Args are " + Arrays.toString(args));
				Object result = null;
				try
				{
					result = targetMethod.invoke(bean.getBean(), args);
				}
				catch (Throwable e)
				{
					while (e.getCause() != null)
					{
						e = e.getCause();
					}
					LOGGER.error("Failed to invoke " + targetMethod.getName());
					throw e;
				}

				JAXBContext context = JAXBContext.newInstance(targetMethod.getReturnType());
				Marshaller m = context.createMarshaller();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				m.marshal(result, os);
				return new String(os.toByteArray());
			}
			catch (Throwable e)
			{
				LOGGER.error("Failed to doExChangeStream." + e.getMessage());
				throw e;
			}
		}
		LOGGER.error("The content-type of Req is not text/xml. Please check.");
		return null;
	}

	private boolean isPrimitive(Class<?> clazz)
	{
		boolean result = clazz.isPrimitive();
		if (!result)
		{
			return primivateClazz.contains(clazz);
		}
		else
		{
			return true;
		}
	}

	private Object createPrimitiveVal(String textTrim, Class<?> clazz)
	{
		if (textTrim == null)
		{
			return null;
		}

		try
		{
			if (clazz == String.class)
			{
				return textTrim;
			}
			else if (clazz == Integer.class || clazz == int.class)
			{
				return Integer.valueOf(textTrim);
			}
			else if (clazz == Double.class || clazz == double.class)
			{
				return Double.valueOf(textTrim);
			}
			else if (clazz == Character.class || clazz == char.class)
			{
				return Character.valueOf(textTrim.charAt(0));
			}
			else if (clazz == Byte.class || clazz == byte.class)
			{
				return Byte.valueOf(textTrim);
			}
			else if (clazz == Short.class || clazz == short.class)
			{
				return Short.valueOf(textTrim);
			}
			else if (clazz == Long.class || clazz == long.class)
			{
				return Long.valueOf(textTrim);
			}
			else if (clazz == Float.class || clazz == float.class)
			{
				return Float.valueOf(textTrim);
			}
			else if (clazz == Boolean.class || clazz == boolean.class)
			{
				return Boolean.valueOf(textTrim);
			}
		}
		catch (NumberFormatException e)
		{
			LOGGER.warn("Format error:" + e.getMessage());
		}
		return null;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setStatus(403);
	}
}
