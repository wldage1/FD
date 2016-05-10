package com.sw.core.exception;

public class DetailException
{
	@SuppressWarnings("rawtypes")
	public static String expDetail(Exception ex, Class className)
	{ 
		ex.printStackTrace();
		StringBuffer buf = new StringBuffer();
		StackTraceElement st[] = ex.getStackTrace();
		for (int i = 0; i < st.length; i++)
		{
			if (st[i].getClassName().equals(className.getName()))
			{
				buf.append("\n错误信息是：")
				.append(ex.getMessage())
				.append("\n")
				.append("产生错误的文件是：")
				.append(st[i].getFileName())
				.append("\n").append("产生错误的类是：")
				.append(st[i].getClassName())
				.append("\n")
				.append("产生错误的方法是：")
				.append(st[i].getMethodName())
				.append("\n")
				.append("错误位于：第")
				.append(st[i].getLineNumber())
				.append("行\n");
				break;
			}
		}
		return buf.toString();
	}
}
