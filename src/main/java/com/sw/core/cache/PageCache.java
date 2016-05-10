package com.sw.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;

import com.sw.core.data.entity.Page;

/**
 * 系统级参数缓存
 * @author new
 *
 */
public class PageCache {
	
	private static final String PAGE_NAME = "page_config";
	
	private static Cache cache = CacheManager.getInstance().getCache(PAGE_NAME);

	public static synchronized void putAuthCache(Page page) {
		if (page != null){
			Element element = new Element(page.getUrl(), page);
			cache.put(element);
		}
	}
	
	public static synchronized Page getCache(Page page) {
		Element element = null;
		try {
			element = cache.get(page.getUrl());
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((Page) element.getValue());
	}
	
	public static synchronized Page getCache(String key) {
		Element element = null;
		try {
			element = cache.get(key);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: "
					+ cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((Page) element.getValue());
	}

	public static synchronized void removeCache(String page) {
		cache.remove(page);
	}

	public static synchronized void removeAllCache() {
		cache.removeAll();
		cache.clearStatistics();
		cache.flush();
	}
}