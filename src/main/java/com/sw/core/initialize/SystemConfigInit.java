package com.sw.core.initialize;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.sw.core.cache.SystemSettingCache;
import com.sw.core.data.entity.Setting;
import com.sw.core.exception.DetailException;

public class SystemConfigInit{
	
	private static Logger logger = Logger.getLogger(SystemConfigInit.class);
	/**
	 * 系统类存放路径目录名
	 */
	private static String WEB_INF = "WEB-INF";
	private Resource configLocation;

	public void init() throws ServletException
	{
		try
		{
			Properties props = new Properties();
	        props.load(configLocation.getInputStream());			
			logger.debug("system config initializing , please waiting...");
			//读取系统debug状态参数
			String isDebug = props.getProperty("system.is.debug") == null ?"":props.getProperty("system.is.debug");
			Setting setting = new Setting();
			if (isDebug.equals("true")){
				setting.setDebug(true);
			}
			else{
				setting.setDebug(false);
			}
			//读取设置系统绝对路径
			try {
				if (configLocation != null){
					String realPath = null;
					String parentPath = configLocation.getFile().getParent();
					if (parentPath != null) {
						int cindex = parentPath.indexOf(WEB_INF);
						if (cindex > -1) {
							realPath = parentPath.substring(0, cindex + WEB_INF.length());
						}
					}
					setting.setRealPath(realPath);
				}
			} catch (IOException e1) {
				try {
					throw new Exception("file path not find!");
				} catch (Exception e) {
					logger.debug(e.getMessage());
					System.exit(0);
				}
			}
			//存储setting对象到缓存中
			SystemSettingCache.putAuthCache(setting);
    		logger.debug("system config initialize finish.");
		}
		catch (Exception e)
		{
			logger.debug("system config initialize fail！");
			String debug = DetailException.expDetail(e, SystemConfigInit.class);
			logger.debug(debug);
			/**异常退出系统*/
			System.exit(0);
		}
	}
    
	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
}
