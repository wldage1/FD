package com.sw.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;

import com.sw.core.data.entity.Setting;

/**
 * 系统级参数缓存
 * @author new
 *
 */
public class SystemSettingCache {
	
	private static final String SYSTEM_SETTING_NAME = "setting_config";
	public static final String SYSTEM_SETTING_KEY = "setting";
	
	private static Cache cache = CacheManager.getInstance().getCache(SYSTEM_SETTING_NAME);

	public static synchronized void putAuthCache(Setting setting) {
		if (setting != null){
			Element element = new Element(SYSTEM_SETTING_KEY, setting);
			cache.put(element);
		}
	}
	public static synchronized Setting getCache(String setting) {
		Element element = null;
		try {
			element = cache.get(setting);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((Setting) element.getValue());
	}

	public static synchronized void removeCache(String code) {
		cache.remove(code);
	}

	public static synchronized void removeAllCache() {
		cache.removeAll();
		cache.clearStatistics();
		cache.flush();
	}
}