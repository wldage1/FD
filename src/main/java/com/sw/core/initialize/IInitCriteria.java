package com.sw.core.initialize;

import javax.servlet.ServletException;

public interface IInitCriteria
{
	/**
	 * 系统配置初始化统一接口
	 * @throws ServletException 
	 */
	public void init(InitData initData) throws Exception;
}
 