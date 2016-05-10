package com.sw.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;

import com.opensymphony.oscache.util.StringUtil;
import com.sw.core.data.entity.Authorization;

public class PluginsConfigCache {

	private static final String PLUGIN_CACHE_AUTH_NAME = "plugin_auth_config";

	private static Cache authCache = CacheManager.getInstance().getCache(PLUGIN_CACHE_AUTH_NAME);

	public static synchronized void putAuthCache(Authorization authorization) {
		if (authorization != null && !StringUtil.isEmpty(authorization.getCode())) {
			Element element = new Element(authorization.getCode(), authorization);
			authCache.put(element);
		}
	}

	public static synchronized Authorization getAuthCache(String code) {
		Element element = null;
		try {
			element = authCache.get(code);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: " + cacheException.getMessage(), cacheException);
		}
		if (element == null)
			return null;

		return ((Authorization) element.getValue());
	}

	public static synchronized void removeAuthCache(String code) {
		authCache.remove(code);
	}

	public static synchronized void removeAllCache() {
		authCache.removeAll();
		authCache.clearStatistics();
		authCache.flush();
	}

	@SuppressWarnings("unchecked")
	public static synchronized Collection<Authorization> getAllAuthCache() {
		Collection<String> resources;
		List<Authorization> resclist = new ArrayList<Authorization>();
		try {
			resources = authCache.getKeys();
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (CacheException e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
		for (Iterator<String> localIterator = resources.iterator(); localIterator.hasNext();) {
			String code = localIterator.next();
			Authorization authorization = getAuthCache(code);
			resclist.add(authorization);
		}
		return resclist;
	}
}