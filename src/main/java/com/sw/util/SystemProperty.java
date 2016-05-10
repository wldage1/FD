package com.sw.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * SPRING property
 * 
 * @author Administrator
 */
public class SystemProperty {
	
	private static Logger logger = Logger.getLogger(SystemProperty.class);

	private static SystemProperty instance = new SystemProperty();

	@SuppressWarnings("rawtypes")
	private static HashMap mgrs = new HashMap();

	private Properties props;

	private SystemProperty() {
	}

	public static SystemProperty getInstance() {
		return instance;
	}

	public static synchronized SystemProperty getInstance(String propertyFileName) {
		SystemProperty item = (SystemProperty) mgrs.get(propertyFileName);
		if (item == null) {
			return new SystemProperty(propertyFileName);
		} else {
			return item;
		}

	}

	private SystemProperty(String propertyFileName) {

		InputStream is = this.getClass().getResourceAsStream("/" + propertyFileName + ".properties");
		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public String getProperties(String fileName, String propertyName) {
		String propertyValue = "";
		Properties prop = new Properties();
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
			prop.load(is);
			if (is != null) {
				is.close();
			}
			propertyValue = prop.getProperty(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			System.out.println("file:" + fileName + " not found");
		}
		return propertyValue;
	}
}
